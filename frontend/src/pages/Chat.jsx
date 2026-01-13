import { useNavigate, useParams, useLocation } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import axios from "axios";
import { ArrowLeftIcon, VideoCameraIcon, PhoneIcon, PaperAirplaneIcon } from "@heroicons/react/24/solid";

import { MESSAGE, MessageType, Call_States } from '../domain/message';




export default function Chat() {
  const navigate = useNavigate();
  const { chatId  } = useParams(); // chatId
  const [messages, setMessages] = useState([]);
  const [myUserId, setMyUserId] = useState(null);
  const [newMessage, setNewMessage] = useState("");
  const location = useLocation();
  const otherUserName = location.state?.otherUserName || "Unknown";

  console.log(chatId);

  const wsRef = useRef(null);


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

      ws.onmessage = e => {
      
        console.log("printing on message from before parsing server: ");
        console.log(`here is my user is ${myUserId}`);
        
        const msg = JSON.parse(e.data);
        console.log("printing on message from server: " + msg);
        
        console.log(msg)
        // setMessages(prev => [
        //   ...prev,
        //   {
        //     id: msg.id,
        //     text: msg.text,
        //     sender: msg.senderId === myUserId ? "me" : "other",
        //   },
        // ]);

        setMessages(prev => [
          ...prev,
          {
            id: msg.id,
            text: msg.text,
            senderId: msg.senderId === myUserId ? "me" : "other",
          },
        ]);
  };

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
      conversationId: chatId
    }

    console.log(MessageType.CHAT + " Type and now actual message: " + newMessage);
    const textMessage = MESSAGE(MessageType.CHAT, newMessage,metaOverrides);
    /**  const meta = {
    conversationId: 0,
    senderId: 0,
    timestamp: Date.now(),
    callId: 0,
    ...metaOverrides,
  }; */

    console.log(textMessage);
    
    wsRef.current?.send(JSON.stringify(textMessage));
    setNewMessage("");


  };



  return (
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
          {otherUserName.slice(0,2).toUpperCase()}
        </div>

        <div className="flex-1">
          <h1 className="font-medium text-gray-900">{ otherUserName.toUpperCase()}</h1>
        </div>
        <div className="flex items-center gap-3 ml-4">
          <button
            onClick={()=> handleAudio}
          > <PhoneIcon className="w-6 h-6" /> </button>
          <button
            onClick={()=> handleVideo}
          > <VideoCameraIcon className="w-6 h-6 mr-3" /> </button>
        </div>
      </header>

      <div className="flex-1 overflow-y-auto px-4 py-3 space-y-3">
        {messages
          .filter(msg => msg.text !== "First Message")
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
  );
}
