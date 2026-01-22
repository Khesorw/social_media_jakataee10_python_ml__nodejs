import { useState } from "react";

import {
  MESSAGE,
  MessageType,
  Call_States,
  Call_IntentType,
  RepondIncomingCall,
  EndReasons,
} from "../../domain/message";

import {
  MicrophoneIcon,
  PhoneXMarkIcon,
  SpeakerWaveIcon,
  SpeakerXMarkIcon,
  UserIcon,
} from "@heroicons/react/24/solid";

import SimulateIncomingCall from "./SimulateIncomingCall";

/**
 *
 * @param {*} param0
 * @returns
 */

const ActiveCall = ({
  callState,
  setCallState,
  wsRef,
  conversationId,
  myUserId,
  callSessionRef,
  localStreamRef,
  localVideoRef,
  remoteVideoRef,
  audioRef,
}) => {
  const [isMuted, setIsMuted] = useState(false);

  console.log(callState);

  //hangup
  const handleHangup = () => {
    const metaOveride = {
      conversationId: conversationId,
      senderId: myUserId,
      callId: callSessionRef.current.callId,
    };
    const hangupMessage = MESSAGE(
      MessageType.CALL_END,
      EndReasons.HANGUP,
      metaOveride,
    );

    //local audio and ref cleanup
    cleanupLocalVideoAndAudio();

    wsRef.current.send(JSON.stringify(hangupMessage));
    console.log("metaOver ride of activeCall ", metaOveride);
    setCallState(Call_States.ENDED);
  }; //hangup

  const cleanupLocalVideoAndAudio = () => {
    if (audioRef.current) {
      audioRef.current.srcObject = null;
    }

    if (localVideoRef.current) {
      localVideoRef.current.srcObject = null;
    }

    if (remoteVideoRef.current?.srcObject) {
      remoteVideoRef.current.srcObject = null;
    }
  };

  //handle mute
  const handleMute = () => {
    const newMute = !isMuted;

    localStreamRef.current?.getAudioTracks().forEach((track) => {
      track.enabled = !newMute;
    });

    setIsMuted(newMute);

    console.log(`🔇 audio ${newMute ? "muted" : "not mute"}`);
  };

  return (
    <>
      {callState === Call_States.INCOMING ? (
        <SimulateIncomingCall
          wsRef={wsRef}
          MESSAGE={MESSAGE}
          callSessionRef={callSessionRef}
          Call_States={Call_States}
          setCallState={setCallState}
          MessageType={MessageType}
          RepondIncomingCall={RepondIncomingCall}
          conversationId={conversationId}
          myUserId={myUserId}
        />
      ) : (
        <div className="relative h-screen w-screen overflow-hidden bg-gray-900 text-white">
          {/* VIDEO LAYER (behind everything) */}
          {callSessionRef.current?.callType === Call_IntentType.VIDEO_CALL ? (
            <>
              <video
                ref={remoteVideoRef}
                autoPlay
                playsInline
                className="absolute inset-0 h-full w-full object-cover bg-black"
              />
              <video
                ref={localVideoRef}
                autoPlay
                muted
                playsInline
                className="absolute bottom-4 right-4 z-20 w-40 aspect-video rounded-lg object-cover bg-black shadow-lg"
              />
            </>
          ) : (
            <audio ref={audioRef} autoPlay playsInline />
          )}

          <div className="relative z-10 flex h-full flex-col">
            {/* SPACER — pushes content down */}
            <div className="flex-1" />

            {/* CONTROLS — pinned to bottom */}
            <div className="pb-10">
              <div className="flex items-center justify-center gap-6">
                <button
                  onClick={handleMute}
                  className={`flex h-14 w-14 items-center justify-center rounded-full border transition
          ${
            isMuted
              ? "bg-white text-gray-900"
              : "border-white text-white hover:bg-white/10"
          }`}
                >
                  {isMuted ? (
                    <SpeakerXMarkIcon className="h-6 w-6" />
                  ) : (
                    <MicrophoneIcon className="h-6 w-6" />
                  )}
                </button>

                <button
                  onClick={handleHangup}
                  className="flex h-16 w-16 items-center justify-center rounded-full bg-red-600 shadow-lg hover:bg-red-700"
                >
                  <PhoneXMarkIcon className="h-7 w-7 text-white" />
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default ActiveCall;
