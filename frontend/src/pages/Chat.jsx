import { useNavigate, useParams } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import axios from "axios";

const mockMessages = [];

export default function Chat() {
  const navigate = useNavigate();
  const { chatId  } = useParams(); // chatId
  const [messages, setMessages] = useState([]);
  const [myUserId, setMyUserId] = useState(null);
  const [newMessage, setNewMessage] = useState("");

  console.log(chatId);

  const wsRef = useRef(null);


useEffect(() => {
  axios
    .get("/corechat/core/me")
    .then(res => setMyUserId(res.data.id))
    .catch(() => navigate("/login"));
}, []);


useEffect(() => {
  if (!myUserId) return;

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


useEffect(() => {
  if (!myUserId) return;

  const ws = new WebSocket(`ws://localhost:8080/corechat/chat/${chatId}`);
  wsRef.current = ws;

  ws.onmessage = e => {
    setMessages(prev => [
      ...prev,
      { id: null, text: e.data, sender: "other" },
    ]);
  };

  return () => ws.close();
}, [chatId, myUserId]);



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

    
    wsRef.current?.send(newMessage);
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
          ←
        </button>

        <div className="w-10 h-10 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-600 font-semibold">
          D
        </div>

        <div className="flex-1">
          <h1 className="font-medium text-gray-900">Diana</h1>
          <p className="text-xs text-gray-500">Online</p>
        </div>

        <button className="text-xl text-gray-500">⋮</button>
      </header>

      {/* Messages */}
      <div className="flex-1 overflow-y-auto px-4 py-3 space-y-3">
        {messages.map((msg, i) => (
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
          ➤
        </button>
      </div>
    </div>
  );
}
