import React, { useEffect, useRef, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { MESSAGE, MessageType, Call_States, Call_IntentType, RepondIncomingCall,EndReasons, normalizeEndReason} from '../../domain/message';
import { PhoneIcon, XMarkIcon }  from "@heroicons/react/24/solid";


 

import {
  MicrophoneIcon,
  PhoneXMarkIcon,
  SpeakerWaveIcon,
  SpeakerXMarkIcon,
  UserIcon,
} from "@heroicons/react/24/solid";

                                                              
const ActiveCall = ({ callState, setCallState, wsRef , conversationId, myUserId, callSessionRef}) => {
 
  const location = useLocation();

  const {
    contactName = "Unknown",
    callType = "audio",
  } = location.state || {};

  const [isMuted, setIsMuted] = useState(false);

  

  console.log(callState);



  /**
   * 
   * @returns Incoming (accept/reject) call component
   */
  const SimulateIncomingCall = () => {

    const audioRef = useRef(null);
    const audioTimer = useRef(null);


    useEffect(() => {
      const ringtone = new Audio('https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3');
      ringtone.loop = true;
      audioRef.current = ringtone;
      ringtone.play().catch(error => {
        console.log("error", error);
      });

      audioTimer.current = setTimeout(() => {
        
        stopAudio();
        console.log("Stopped ringtone")

      }, 40000);

    }, []);

    const stopAudio = () => {
      if (audioRef.current) {
        audioRef.current.pause();
        audioRef.current.currentTime = 0;
        audioRef.current = null;
      }

      if (audioTimer.current) {
        clearTimeout(audioTimer.current);
      }
   
    }//stopAudio()   
      
    const onReject = () => {

      console.log("call session from the ref in the onReject is : ", callSessionRef);

      stopAudio();
      const metaOveride = {
        conversationId: conversationId,
        senderId: myUserId,
        callId: callSessionRef.current.callId,
      };

      const rejectedMessage = MESSAGE(MessageType.CALL_RESPONSE, RepondIncomingCall.REJECT, metaOveride);
      wsRef.current?.send(JSON.stringify(rejectedMessage));
      console.log('metaOver ride of activeCall ', metaOveride);
      setCallState(Call_States.ENDED); 
      
      
    }//onReject ()


    const onAccept = () => {
      stopAudio();
      
      const metaOveride = {
        conversationId: conversationId,
        senderId: myUserId,
        callId: callSessionRef.current.callId,
      };

      const acceptedMessage = MESSAGE(MessageType.CALL_RESPONSE, RepondIncomingCall.ACCEPT, metaOveride);
      wsRef.current?.send(JSON.stringify(acceptedMessage));
      console.log('metaOver ride of activeCall ', metaOveride);


      setCallState(Call_States.ACTIVE);

      
    }//onAccept()

    /**
     * send hangup message to caller with appropriate end reason
     * set the callid of receiver to 0
     * set the call state back to idle
     */
    const handleHangup = () => {
      const metaOveride = {  
        conversationId: conversationId,
        senderId: myUserId,
        callId: callSessionRef.current.callId,

      };
      const hangupMessage = MESSAGE(MessageType.CALL_END, EndReasons.HANGUP, metaOveride);
      wsRef.current.send(JSON.stringify(hangupMessage));
      console.log('metaOver ride of activeCall ', metaOveride);
      setCallState(Call_States.ENDED);
    }

    return (
      <div className="h-screen flex flex-col items-center justify-center bg-gray-900 text-white gap-6">
        <h1 className="text-2xl font-semibold">
          Incoming Call
        </h1>

        <p className="text-lg opacity-90">
          callerName
        </p>

        <div className="flex gap-10 mt-6">
          <button
            onClick={onReject}
            className="w-16 h-16 rounded-full bg-red-600 flex items-center justify-center
             animate-vibrate hover:scale-110 transition-transform shadow-lg shadow-red-500/40"
          >
            <XMarkIcon className="w-8 h-8" />
          </button>

          <button
            onClick={onAccept}
            className="w-16 h-16 rounded-full bg-green-600 flex items-center justify-center 
            animate-pulseStrong hover:scale-110 transition-transform shadow-lg shadow-green-500/400"
          >
            <PhoneIcon className="w-8 h-8" />
          </button>
        </div>

      </div>
    );
  }; //SimulateIncomingCall()

  return (
    <>
      {callState === Call_States.INCOMING ? (
        
        <SimulateIncomingCall />
      ) : (
        <div className="relative flex h-screen flex-col bg-gray-900 text-white">

        {/* Caller Info */}
        <div className="flex flex-1 flex-col items-center justify-center">
          {/* Avatar */}
          <div className="mb-6 flex h-36 w-36 items-center justify-center rounded-full bg-gray-700">
            <UserIcon className="h-20 w-20 text-gray-300" />
          </div>

          <h2 className="text-2xl font-semibold">contactName</h2>

          <p className="mt-2 text-sm text-cyan-400">
            {callType === "video" ? "Video Calling..." : "00:15"}
          </p>
        </div>

        {/* Controls */}
        <div className="pb-10">
          <div className="flex items-center justify-center gap-6">

    
            {/* Mute */}
          <button
            onClick={() => setIsMuted(!isMuted)}
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

            {/* Hangup */}
            <button
              onClick={ handleHangup}
              className="flex h-16 w-16 items-center justify-center rounded-full bg-red-600 shadow-lg transition hover:bg-red-700"
            >
              <PhoneXMarkIcon className="h-7 w-7 text-white" />
            </button>

            {/* Speaker */}
            <button
              className="flex h-14 w-14 items-center justify-center rounded-full border border-white text-white transition hover:bg-white/10"
            >
              <SpeakerWaveIcon className="h-6 w-6" />
            </button>

          </div>
        </div>
        </div>
     ) }
    </>
  );
};

export default ActiveCall;
