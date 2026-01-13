
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
  AUDIO_CALL : "audio_call",
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
  return Object.values(EndReasons).includes(reason)
    ? reason
    : EndReasons.ERROR;
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
