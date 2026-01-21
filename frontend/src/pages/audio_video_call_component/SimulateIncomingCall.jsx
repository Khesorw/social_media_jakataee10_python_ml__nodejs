  
import { PhoneIcon, XMarkIcon } from "@heroicons/react/24/solid";
import  { useEffect, useRef} from "react";

  
  /**
   *
   * @returns Incoming (accept/reject) call component
   */
  const SimulateIncomingCall = ({wsRef, Call_States, setCallState, MESSAGE, callSessionRef, MessageType, RepondIncomingCall, conversationId, myUserId}) => {
    const audioRef = useRef(null);
    const audioTimer = useRef(null);

    useEffect(() => {
      const ringtone = new Audio(
        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
      );
      ringtone.loop = true;
      audioRef.current = ringtone;
      ringtone.play().catch((error) => {
        console.log("error", error);
      });

      audioTimer.current = setTimeout(() => {
        stopAudio();
        console.log("Stopped ringtone");
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
    }; //stopAudio()

    const onReject = () => {
      console.log(
        "call session from the ref in the onReject is : ",
        callSessionRef,
      );

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
    }; //onReject ()

    const onAccept = () => {
      stopAudio();

      const metaOveride = {
        conversationId: conversationId,
        senderId: myUserId,
        callId: callSessionRef.current.callId,
      };

      const acceptedMessage = MESSAGE(
        MessageType.CALL_RESPONSE,
        RepondIncomingCall.ACCEPT,
        metaOveride,
      );
      wsRef.current?.send(JSON.stringify(acceptedMessage));
      console.log("metaOver ride of activeCall ", metaOveride);

      setCallState(Call_States.ACTIVE);
    }; //onAccept()

    /**
     * send hangup message to caller with appropriate end reason
     * set the callid of receiver to 0
     * set the call state back to idle
     */

    return (
      <div className="h-screen flex flex-col items-center justify-center bg-gray-900 text-white gap-6">
        <h1 className="text-2xl font-semibold">Incoming Call</h1>

        <p className="text-lg opacity-90">callerName</p>

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
  
export default SimulateIncomingCall;