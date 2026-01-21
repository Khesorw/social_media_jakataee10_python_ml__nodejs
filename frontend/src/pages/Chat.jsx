import { useNavigate, useParams, useLocation } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import axios from "axios";
import {
  ArrowLeftIcon,
  VideoCameraIcon,
  PhoneIcon,
  PaperAirplaneIcon,
} from "@heroicons/react/24/solid";

import {
  MESSAGE,
  MessageType,
  Call_States,
  Call_IntentType,
  RepondIncomingCall,
  getLocalMedia,
  initPeerConnection,
  getMetaOverRide,
  cleanupCall,
} from "../domain/message";
import { nanoid } from "nanoid";

import ActiveCall from "./audio_video_call_component/ActiveCall";

export default function Chat() {
  const navigate = useNavigate();
  const { chatId } = useParams(); // chatId
  const [messages, setMessages] = useState([]);
  const [myUserId, setMyUserId] = useState(null);
  const [newMessage, setNewMessage] = useState("");
  const location = useLocation();
  const otherUserName = location.state?.otherUserName || "Unknown";
  const [callState, setCallState] = useState(Call_States.IDLE);
  const callSessionRef = useRef(null);
  const pcRef = useRef(null);
  const localStreamRef = useRef(null);
  const remoteStreamRef = useRef(new MediaStream());
  const pendCandRef = useRef(null);
  const callStateRef = useRef(Call_States.IDLE);
  const localVideoRef = useRef(null);
  const remoteVideoRef = useRef(null);
  const audioRef = useRef(null);

  const setNewCallState = (value) => {
    setCallState(value);
    callStateRef.current = value;
  };

  //cleanup call useEffect clean the session and set callState back it IDLE if call is not already cleaned
  useEffect(() => {
    console.log("cleanup useEffect is being rendered before condion check");
    console.log("call Session is: ", callSessionRef);
    console.log("call state is ", callState);
    if (callState !== Call_States.ENDED) return;
    if (!callSessionRef.current) return;

    console.log("cleanup useEffect is being rendered after condion check");

    cleanupCall(
      localStreamRef,
      remoteStreamRef,
      pcRef,
      pendCandRef,
      callSessionRef,
      setCallState,
    );
    console.log("call Session is: ", callSessionRef);
    console.log("call state is ", callState);
  }, [callState]);

  console.log(chatId);
  const wsRef = useRef(null);

  const ActiveCallState = new Set([Call_States.INCOMING, Call_States.ACTIVE]);

  useEffect(() => {
    axios
      .get("/corechat/core/me")
      .then((res) => setMyUserId(res.data.id))
      .catch(() => navigate("/login"));
  }, []);

  //load the messages for this chatId
  useEffect(() => {
    axios.get(`/corechat/core/conversation/${chatId}/messages`).then((res) => {
      const mapped = res.data.map((m) => ({
        id: m.id,
        text: m.text,
        createdAt: m.createdAt,
        sender: m.senderId === myUserId ? "me" : "other",
      }));
      setMessages(mapped);
    });
  }, [chatId, myUserId]);

  //webSocket Connection useEffect
  useEffect(() => {
    if (!myUserId) return;
    console.log("connected to websocket before");

    //change ws protocol based on type of connection (http || https)
    const wsProtocol = window.location.protocol === "https:" ? "wss" : "ws";
    const ws = new WebSocket(`${wsProtocol}://${window.location.host}/corechat/chat/${chatId}`);
    wsRef.current = ws;
    console.log("connected to websocket after");
    ws.onmessage = async (e) => {
      const msg = JSON.parse(e.data);
      console.log("printing on message from server: ", msg);
      console.log(msg);

      console.log("the type of the message is: " + msg.type);

      switch (msg.type) {
        case "chat_message":
          setMessages((prev) => [
            ...prev,
            {
              id: msg.id,
              text: msg.text,
              senderId: msg.senderId === myUserId ? "me" : "other",
            },
          ]);
          break;

        case "call_intent":
          console.log("It is call intent");
          console.log("it is callintent msg ", msg);
          if (!callSessionRef.current) {
            const newCallSession = {
              callId: msg.meta.callId,
              otherUserId: msg.meta.senderId,
              callType:
                msg.payload.callType === Call_IntentType.AUDIO_CALL
                  ? Call_IntentType.AUDIO_CALL
                  : Call_IntentType.VIDEO_CALL,
              callState: Call_States.INCOMING,
            };
            console.log("setting new call session");
            //set new callSession
            callSessionRef.current = newCallSession;
            console.log("new Call session is: ", callSessionRef.current);

            //set callSate to incoming (render the incoming call ui)
            setCallState(Call_States.INCOMING);

            console.log(
              "new Call sesssion is with incoming call state",
              newCallSession,
            );
          } //if()
          else {
            console.log("callSession is already established");
          }

          break;

        case "call_response":
          if (msg.meta.callId == callSessionRef.current.callId) {
            console.log("RENDER from response ", callState);
            console.log("It is call respond ", msg);
            console.log("call id of msg.meta.callid is:  ", msg.meta.callId);
            console.log("call session is: ", callSessionRef);

            callSessionRef.current.callState =
              msg.payload.respond === RepondIncomingCall.ACCEPT
                ? Call_States.ACTIVE
                : Call_States.ENDED;
            //setNew callState based on users responds (accept call || reject call)
            msg.payload.respond === RepondIncomingCall.ACCEPT
              ? setNewCallState(Call_States.ACTIVE)
              : setNewCallState(Call_States.ENDED);
            console.log(
              "current call state after accept or reject " +
                callState +
                " callSateRef is " +
                callStateRef.current,
            );

            //if call responds is accept then state of the call is now active and initiate p2p connection
            if (callStateRef.current === Call_States.ACTIVE) {
              console.log(
                "call session is after active call state: ",
                callSessionRef,
              );

              pcRef.current = initPeerConnection(
                wsRef,
                remoteStreamRef,
                getMetaOverRide(chatId, myUserId, callSessionRef),
                remoteVideoRef,
                audioRef,
              );

              const stream = await getLocalMedia(
                localStreamRef,
                callSessionRef.current.callType,
              );
              stream.getTracks().forEach((track) => {
                pcRef.current.addTrack(track, stream);
              });

              if (localVideoRef.current) {
                localVideoRef.current.srcObject = stream;
              }

              const offer = await pcRef.current.createOffer();
              pcRef.current.setLocalDescription(offer);
              wsRef.current.send(
                JSON.stringify(
                  MESSAGE(
                    MessageType.CALL_OFFER,
                    offer,
                    getMetaOverRide(chatId, myUserId, callSessionRef),
                  ),
                ),
              );
            } //if respond == accept
          } //end if

          break;

        case "call_offer":
          console.log("offer type");
          console.log("offer from caller is: ", msg);
          console.log(getMetaOverRide(chatId, myUserId, callSessionRef));

          console.log("call sessionREf is ", callSessionRef.current);
          pcRef.current ??= initPeerConnection(
            wsRef,
            remoteStreamRef,
            getMetaOverRide(chatId, myUserId, callSessionRef),
            remoteVideoRef,
            audioRef,
          );
          await pcRef.current.setRemoteDescription(msg.payload.sdp);
          console.log(
            "checking the call offer callsion ",
            callSessionRef.current,
          );
          const stream = await getLocalMedia(
            localStreamRef,
            callSessionRef.current.callType,
          );
          stream
            .getTracks()
            .forEach((track) => pcRef.current.addTrack(track, stream));

          if (localVideoRef.current) {
            localVideoRef.current.srcObject = stream;
          }

          const answer = await pcRef.current.createAnswer();

          console.log("answer is ", answer);
          await pcRef.current.setLocalDescription(answer);
          wsRef.current.send(
            JSON.stringify(
              MESSAGE(
                MessageType.CALL_ANSWER,
                answer,
                getMetaOverRide(chatId, myUserId, callSessionRef),
              ),
            ),
          );
          (pendCandRef.current || []).forEach((c) =>
            pcRef.current.addIceCandidate(c),
          );
          pendCandRef.current = [];

          break;

        case "call_answer":
          console.log("answer msessage type");
          console.log(" hello prinintg answer from callee ", msg.payload.sdp);
          await pcRef.current.setRemoteDescription(msg.payload.sdp);
          (pendCandRef.current || []).forEach((c) =>
            pcRef.current.addIceCandidate(c),
          );
          pendCandRef.current = [];
          break;

        case "ice_candidate":
          console.log("ice canditae type");
          console.log("ice candidate message from onice ", msg);
          if (msg.meta.callId !== callSessionRef.current.callId) return;
          const candidate = new RTCIceCandidate(msg.payload.candidate);
          if (
            pcRef.current &&
            pcRef.current.remoteDescription &&
            pcRef.current.remoteDescription.type
          ) {
            pcRef.current.addIceCandidate(candidate);
          } else {
            pendCandRef.current = pendCandRef.current || [];
            pendCandRef.current.push(candidate);
          }
          break;

        case "call_end":
          console.log("end call message", msg);
          if (msg.meta.callId === callSessionRef.current.callId) {
            setCallState(Call_States.ENDED);
          }
          console.log("end call case");
          break;

        default:
          console.error("Something wrong happened");
      }
    }; //onMessage()

    return () => ws.close();
  }, [chatId, myUserId]); //ws useeffect

  //sends message first update ui then send it to server through websocket
  const handleSend = () => {
    if (!newMessage.trim()) return;

    setMessages((prev) => [
      ...prev,
      {
        id: null,
        sender: "me",
        text: newMessage,
      },
    ]);

    const metaOverrides = {
      senderId: myUserId,
      conversationId: chatId,
    };

    console.log(
      MessageType.CHAT + " Type and now actual message: " + newMessage,
    );
    const textMessage = MESSAGE(MessageType.CHAT, newMessage, metaOverrides);

    console.log(textMessage);

    wsRef.current?.send(JSON.stringify(textMessage));
    setNewMessage("");
  }; //handleSend()

  const handleVideo = () => {
    console.log("call state right now is ", callState);
    if (callState === Call_States.IDLE) {
      const sessionOverrider = {
        callId: nanoid(10), //unique callId with the length = 10
        callState: Call_States.OUTGOING,
        callType: Call_IntentType.VIDEO_CALL,
        isCaller: true,
      };

      const MetaOveride = {
        conversationId: chatId,
        senderId: myUserId,
        callId: sessionOverrider.callId,
      };

      const videoMessage = MESSAGE(
        MessageType.CALL_INTENT,
        Call_IntentType.VIDEO_CALL,
        MetaOveride,
      );
      wsRef.current?.send(JSON.stringify(videoMessage));

      callSessionRef.current = sessionOverrider;
      console.log(
        "wrote new session for the video call ",
        callSessionRef.current,
      );
      console.log("now printing the sessionOverRider ", sessionOverrider);
      setCallState(Call_States.OUTGOING);
    } //end if
    else {
      console.log("already outgoing call state");
    }
  };

  /**
   * if call session is create new call session (callId, callType, isCaller), change the sate of call to OUTGOING, then send the MESSAGE that
   * conatains call_intent with meta
   */
  const handleAudio = () => {
    console.log("call state right now is ", callState);
    if (callState === Call_States.IDLE) {
      const sessionOverrider = {
        callId: nanoid(10), //unique callId with the length = 10
        callState: Call_States.OUTGOING,
        callType: Call_IntentType.AUDIO_CALL,
        isCaller: true,
      };

      console.log("new call sessiong from handelAudio call ", sessionOverrider);

      const metaOveride = {
        conversationId: chatId,
        senderId: myUserId,
        callId: sessionOverrider.callId,
      };

      console.log(metaOveride);

      const audioMessage = MESSAGE(
        MessageType.CALL_INTENT,
        Call_IntentType.AUDIO_CALL,
        metaOveride,
      );

      wsRef.current?.send(JSON.stringify(audioMessage));

      // setCallSession(sessionOverrider);
      callSessionRef.current = sessionOverrider;
      console.log(
        "call session and call sate is before setting state to outgoing ",
        { callSessionRef, callState },
      );
      setCallState(Call_States.OUTGOING);
      console.log("call session and call sate is: ", {
        callSessionRef,
        callState,
      });
    } //if()
    else {
      console.log("already outgoing call state");
    }
  }; //handleAudioCall()
  //callState, setCallState, wsRef

  return (
    <>
      {ActiveCallState.has(callState) ? (
        <ActiveCall
          callState={callState}
          setCallState={setCallState}
          wsRef={wsRef}
          conversationId={chatId}
          myUserId={myUserId}
          callSessionRef={callSessionRef}
          remoteStreamRef={remoteStreamRef}
          localStreamRef={localStreamRef}
          localVideoRef={localVideoRef}
          remoteVideoRef={remoteVideoRef}
          audioRef={audioRef}
        />
      ) : (
        <div className="h-screen flex flex-col bg-gray-50">
          {/* Header */}
          <header className="flex items-center gap-3 px-4 py-3 bg-white border-b">
            <button onClick={() => navigate("/feed")} className="text-xl">
              <ArrowLeftIcon className="w-5 h-5" />
            </button>
            <div className="w-10 h-10 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-600 font-semibold">
              {otherUserName.slice(0, 2).toUpperCase()}
            </div>

            <div className="flex-1">
              <h1 className="font-medium text-gray-900">
                {otherUserName.toUpperCase()}
              </h1>
            </div>
            <div className="flex items-center gap-3 ml-4">
              <button
                disabled={callState !== Call_States.IDLE}
                onClick={handleAudio}
              >
                {" "}
                <PhoneIcon className="w-6 h-6" />{" "}
              </button>
              <button
                disabled={callState !== Call_States.IDLE}
                onClick={handleVideo}
              >
                {" "}
                <VideoCameraIcon className="w-6 h-6 mr-3" />{" "}
              </button>
            </div>
          </header>

          <div className="flex-1 overflow-y-auto px-4 py-3 space-y-3">
            {messages
              .filter((msg) => msg.text !== "First Message")
              .map((msg, i) => (
                <div
                  key={msg.id ?? i}
                  className={`flex ${
                    msg.sender === "me" ? "justify-end" : "justify-start"
                  }`}
                >
                  <div
                    className={`max-w-[75%] px-4 py-2 rounded-2xl text-sm ${
                      msg.sender === "me"
                        ? "bg-indigo-600 text-white rounded-br-sm"
                        : "bg-white text-gray-900 rounded-bl-sm border"
                    }`}
                  >
                    {msg.text}
                  </div>
                </div>
              ))}
          </div>

          {/* Input */}
          <div className="px-3 py-2 bg-white border-t flex items-center gap-2">
            <button className="text-xl text-gray-500">+</button>

            <input
              type="text"
              placeholder="Type a message"
              className="flex-1 border rounded-full px-4 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-200"
              value={newMessage}
              onChange={(e) => setNewMessage(e.target.value)}
              onKeyDown={(e) => e.key === "Enter" && handleSend()}
            />

            <button
              onClick={handleSend}
              className="text-indigo-600 font-semibold"
            >
              <PaperAirplaneIcon className="w-6 h-6" />
            </button>
          </div>
        </div>
      )}
    </>
  );
}
