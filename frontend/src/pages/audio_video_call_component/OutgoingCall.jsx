import { useEffect, useRef, useState } from "react";
import { PhoneXMarkIcon, UserIcon } from "@heroicons/react/24/solid";
import outgoingSound from "../../assets/audios/outgoing.mp3";

import { Call_States, MESSAGE, MessageType, RepondIncomingCall } from "../../domain/message";

const OutgoinCall = ({
  callState,
  setCallState,
  wsRef,
  callSessionRef,
  myUserId,
    conversationId,
  otherUserName,
}) => {
  const ringToneRef = useRef(null);
  const ringTimeoutRef = useRef(null);

  const startRinging = () => {
    const ringtone = new Audio(outgoingSound);
    ringtone.loop = true;
    ringtone.play().catch((error) => {
      console.log("audio outgoing call blocked ", error);
    });
    ringToneRef.current = ringtone;

    ringTimeoutRef.current = setTimeout(() => {
      stopAudio();
      onCancel();
    }, 40000);

    return ringtone;
  }; //startRinging()

  const stopAudio = () => {
    if (ringToneRef.current) {
      ringToneRef.current.pause();
      ringToneRef.current.currentTime = 0;
      ringToneRef.current = null;
    }

    if (ringTimeoutRef.current) {
      clearTimeout(ringTimeoutRef.current);
      ringTimeoutRef.current = null;
    }
  }; //stopAudio()

  useEffect(() => {
    if (callState === Call_States.OUTGOING) {
      startRinging();
    }

    return () => {
      stopAudio();
    };
  }, [callState]);

  const onCancel = () => {
    stopAudio();
    const metaOveride = {
      conversationId: conversationId,
      senderId: myUserId,
      callId: callSessionRef.current.callId,
    };

    const rejectedMessage = MESSAGE(
      MessageType.CALL_RESPONSE,
      RepondIncomingCall.REJECT,
      metaOveride,
    );

    wsRef.current?.send(JSON.stringify(rejectedMessage));
    console.log("metaOver ride of activeCall ", metaOveride);
    setCallState(Call_States.ENDED);
  };

  return (
    <>
      {callState === Call_States.OUTGOING && (
        <div className="h-screen flex flex-col items-center justify-center bg-gray-900 text-white gap-6 relative overflow-hidden">
          <h1 className="text-2xl font-semibold">Calling…</h1>
                  <p className="text-lg opacity-80">{ otherUserName ?? 'unknown'}</p>

          {/* Ripple Container */}
          <div className="relative w-40 h-40 flex items-center justify-center mt-8">
            {/* Ripples */}
            <span className="absolute w-full h-full rounded-full bg-indigo-500/30 animate-ripple"></span>
            <span className="absolute w-full h-full rounded-full bg-indigo-500/20 animate-ripple delay-1000"></span>
            <span className="absolute w-full h-full rounded-full bg-indigo-500/10 animate-ripple delay-2000"></span>

            {/* Caller Icon */}
            <div className="w-20 h-20 rounded-full bg-indigo-600 flex items-center justify-center z-10 shadow-xl">
              <UserIcon className="w-10 h-10" />
            </div>
          </div>

          {/* Cancel Button */}
          <button
            onClick={onCancel}
            className="mt-12 w-16 h-16 rounded-full bg-red-600 flex items-center justify-center
        hover:scale-110 transition-transform shadow-lg shadow-red-500/40"
          >
            <PhoneXMarkIcon className="w-8 h-8" />
          </button>
        </div>
      )}
    </>
  );
};

export default OutgoinCall;
