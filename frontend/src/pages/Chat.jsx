import { useNavigate, useParams, useLocation } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import axios from "axios";
import { ArrowLeftIcon, VideoCameraIcon, PhoneIcon, PaperAirplaneIcon } from "@heroicons/react/24/solid";

import { MESSAGE, MessageType, Call_States, Call_IntentType } from '../domain/message';

import  ActiveCall  from './audio_video_call_component/ActiveCall';

export default function Chat() {
  const navigate = useNavigate();
  const { chatId  } = useParams(); // chatId
  const [messages, setMessages] = useState([]);
  const [myUserId, setMyUserId] = useState(null);
  const [newMessage, setNewMessage] = useState("");
  const location = useLocation();
  const otherUserName = location.state?.otherUserName || "Unknown";
  const [callState, setCallState] = useState(Call_States.IDLE);

  console.log(chatId);
  const wsRef = useRef(null);

  const ActiveCallState = new Set([
    Call_States.INCOMING,
    Call_States.ACTIVE,
  ]);


useEffect(() => {
  axios
    .get("/corechat/core/me")
    .then(res => setMyUserId(res.data.id))
    .catch(() => navigate("/login"));
}, []);


//load the messages for this chatId
useEffect(() => {
  axios
    .get(`/corechat/core/conversation/${chatId}/messages`)
    .then(res => {
      const mapped = res.data.map(m => ({
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
    
      const ws = new WebSocket(`ws://localhost:8080/corechat/chat/${chatId}`);
      wsRef.current = ws;

      ws.onmessage = e=> {
      
        console.log("printing on message from before parsing server: ");
        console.log(`here is my user is ${myUserId}`);

        const msg = JSON.parse(e.data);
        console.log("printing on message from server: ", msg);
        console.log(msg);
        
        console.log("the type of the message is: " + msg.type);

        switch (msg.type) {
          case 'chat_message':
            setMessages(prev => [
              ...prev,
              {
                id: msg.id,
                text: msg.text,
                senderId: msg.senderId === myUserId ? "me" : "other",
              },
            ]);
            break;
          
          case 'call_intent':
            console.log("It is call intent");
            setCallState(Call_States.INCOMING);
            break;
          
          case 'call_response':
            console.log("It is call respond");
            break;
          
          case 'call_offer':
            console.log("offer type");
            break;
          
          case 'call_answer':
            console.log("answer msessage type");
            break;
          
          case 'ice_candidate':
            console.log("ice canditae type");
            break;
          
          case 'call_end':
            console.log("end call case");
            break;
          
          default:
            console.error("Something wrong happened");
            
        }

  };//onMessage()

    
  return () => ws.close();

}, [chatId, myUserId]);


  //sends message first update ui then send it to server through websocket
  const handleSend = () => {
    if (!newMessage.trim()) return;

    setMessages(prev => [
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

    console.log(MessageType.CHAT + " Type and now actual message: " + newMessage);
    const textMessage = MESSAGE(MessageType.CHAT, newMessage,metaOverrides);

    console.log(textMessage);
    
    wsRef.current?.send(JSON.stringify(textMessage));
    setNewMessage("");


  };//handleSend()

  const handleVideo = () => {
  setCallState(Call_States.OUTGOING);
    
  const MetaOveride = {
      conversationId: chatId,
      senderId: myUserId,
    };

   const videoMessage = MESSAGE(MessageType.CALL_INTENT, Call_IntentType.VIDEO_CALL, MetaOveride);
   wsRef.current?.send(JSON.stringify(videoMessage));
    
    
  }//handleVideoCall()
  
  const handleAudio = () => {
    //temporary checking the audio on incoming call will change back to outgoing after test
    setCallState(Call_States.OUTGOING);

    const MetaOveride = {
      conversationId: chatId,
      senderId: myUserId,
    };

   const  audioMessage = MESSAGE(MessageType.CALL_INTENT, Call_IntentType.AUDIO_CALL, MetaOveride);
   wsRef.current?.send(JSON.stringify(audioMessage));
    
  }//handleAudioCall()
//callState, setCallState, wsRef

  return (
    <>
      {ActiveCallState.has(callState)? (
        <ActiveCall callState={callState} setCallState={setCallState} wsRef={wsRef} conversationId={chatId} myUserId={myUserId} />
      ) : (
        <div className="h-screen flex flex-col bg-gray-50">

          {/* Header */}
          <header className="flex items-center gap-3 px-4 py-3 bg-white border-b">
            <button
              onClick={() => navigate("/feed")}
              className="text-xl"
            >
              <ArrowLeftIcon className="w-5 h-5" />
            </button>
            <div className="w-10 h-10 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-600 font-semibold">
              {otherUserName.slice(0, 2).toUpperCase()}
            </div>

            <div className="flex-1">
              <h1 className="font-medium text-gray-900">{otherUserName.toUpperCase()}</h1>
            </div>
            <div className="flex items-center gap-3 ml-4">
              <button
                disabled={callState !== Call_States.IDLE}
                onClick={handleAudio}
              > <PhoneIcon className="w-6 h-6" /> </button>
              <button
                disabled={callState !== Call_States.IDLE }
                onClick={handleVideo}
              > <VideoCameraIcon className="w-6 h-6 mr-3" /> </button>
            </div>
          </header>

          <div className="flex-1 overflow-y-auto px-4 py-3 space-y-3">
            {messages
              .filter(msg => msg.text !== "First Message")
              .map((msg, i) => (
                <div
                  key={msg.id ?? i}
                  className={`flex ${msg.sender === "me" ? "justify-end" : "justify-start"
                    }`}
                >
                  <div
                    className={`max-w-[75%] px-4 py-2 rounded-2xl text-sm ${msg.sender === "me"
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
