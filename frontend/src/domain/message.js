
export const cleanupCall = (localStreamRef, remoteStreamRef,  pcRef, pendCandRef,callSessionRef,setCallState) => {
  console.log("Cleaning up WebRTC resources");

  
  if (localStreamRef.current) {
    localStreamRef.current.getTracks().forEach(track => {
      track.stop();
    });
    localStreamRef.current = null;
  }

  
  if (remoteStreamRef.current) {
    remoteStreamRef.current.getTracks().forEach(track => {
      track.stop();
    });
    remoteStreamRef.current = new MediaStream();
  }


  if (pcRef.current) {
    pcRef.current.onicecandidate = null;
    pcRef.current.ontrack = null;
    pcRef.current.close();
    pcRef.current = null;
  }

  pendCandRef.current = [];

  
  callSessionRef.current = null;

  setCallState(Call_States.IDLE);
};













export const getMetaOverRide = (chatId, myUserId, callSessionRef) => {
  const metaOveride = {
    conversationId: chatId,
    senderId: myUserId,
    callId: callSessionRef?.current.callId,
  };


  return metaOveride;
};

export const initPeerConnection = (wsRef, remoteStreamRef, metaOveride) => {
  const rtcConfig = {
    //stun server
    iceServers: [
      {
        urls: "stun:stun.1.google.com:19302",
      },
    ],
  }; //iceServers

  const pc = new RTCPeerConnection(rtcConfig);
  
  pc.onicecandidate = (event) => {
    if (event.candidate) {
      const sdpCandidate = MESSAGE(
        MessageType.CANDIDATE,
        event.candidate,
        metaOveride,
      );

      console.log('sending the candidate to other peer: ' + event.candidate);
      wsRef.current?.send(JSON.stringify(sdpCandidate)); //send the offer through ws
    } //end if
  }; //oniceCandidate()

  pc.ontrack = (event) => {
    console.log('getting or printing on track stram. ', event);
    event.streams[0].getTracks().forEach((track) => {
      remoteStreamRef.current.addTrack(track);
    });
  };

  return pc;
}; //createPeerConnection()

export const getLocalMedia = async (localStreamRef, callType) => {

  const video_config = {
    width: { ideal: 1280 },
    height: { ideal: 720 },
    frameRate: { ideal: 30 }
  };
  const stream = await navigator.mediaDevices.getUserMedia({
    audio: true,
    video: callType === Call_IntentType.VIDEO_CALL ? video_config : false,
  });






  localStreamRef.current = stream;

  return stream;
};

//Message Enums
export const Call_States = Object.freeze({
  IDLE: "idle",
  OUTGOING: "outgoing",
  INCOMING: "incoming",
  ACTIVE: "active",
  ENDED: "ended",
});

export const MessageType = Object.freeze({
  CHAT: "chat_message",
  CALL_INTENT: "call_intent",
  CALL_RESPONSE: "call_response",
  CALL_OFFER: "call_offer",
  CALL_ANSWER: "call_answer",
  CANDIDATE: "ice_candidate",
  CALL_END: "call_end",
});

export const Call_IntentType = Object.freeze({
  VIDEO_CALL: "video_call",
  AUDIO_CALL: "audio_call",
});

export const RepondIncomingCall = Object.freeze({
  REJECT: "reject",
  ACCEPT: "accept",
});

export const EndReasons = Object.freeze({
  HANGUP: "hangup",
  REJECTED: "rejected",
  ERROR: "error",
  TIMEOUT: "timeout",
});

//Helper (end reason should be one fom EndReason else error)

export function normalizeEndReason(reason) {
  return Object.values(EndReasons).includes(reason) ? reason : EndReasons.ERROR;
}

export function CreateSession(sessionOverrides = {}) {
  const session = {
    callId: 0,
    callState: null,
    callType: null,
    isCaller: true,
    startedAt: Date.now(),
    ...sessionOverrides,
  };

  return session;
}

//Message Factory

export function MESSAGE(type, payloadData, metaOverrides = {}) {
  const meta = {
    conversationId: 0,
    senderId: 0,
    timestamp: Date.now(),
    callId: 0,
    ...metaOverrides,
  };

  const payloads = {
    [MessageType.CHAT]: { text: payloadData },
    [MessageType.CALL_INTENT]: { callType: payloadData },
    [MessageType.CALL_RESPONSE]: { respond: payloadData },
    [MessageType.CALL_OFFER]: { sdp: payloadData },
    [MessageType.CALL_ANSWER]: { sdp: payloadData },
    [MessageType.CANDIDATE]: { candidate: payloadData },
    [MessageType.CALL_END]: { reason: normalizeEndReason(payloadData) },
  };

  return {
    type,
    meta,
    payload: payloads[type] ?? {},
  };
}
