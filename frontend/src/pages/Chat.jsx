import { useNavigate, useParams } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import axios from "axios";

const mockMessages = [
  {
    id: 1,
    sender: "me",
    text: "Hi Diana!",
  },
  {
    id: 2,
    sender: "other",
    text: "Hey Charlie 👋",
  },

];

export default function Chat() {
  const navigate = useNavigate();
  const { chatId  } = useParams(); // chatId
  const [messages, setMessages] = useState(mockMessages);
  const [newMessage, setNewMessage] = useState("");

  console.log(chatId);

  const wsRef = useRef(null);

  useEffect(() => {
    let ws;

  const connect = async () => {
    try {
      console.log("Checking auth via /me");
      const res = await axios.get("/corechat/core/me");

      if (res.status === 200) {
        console.log("Auth OK → opening WebSocket response data "+res.data);

        ws = new WebSocket("ws://localhost:8080/corechat/chat");
        
        wsRef.current = ws;

        ws.onopen = () => {
          console.log("ws connection opened");
          ws.send("Hello from client");
      
        };

        ws.onmessage = (event) => {
          console.log("WS message:", event.data);

          setMessages(prev =>[
            ...prev,
            {
              id: Date.now(),
              sender: "other",
              text: event.data,
            },
          ]);
        };

        ws.onclose = () => {
          console.log("WS closed");
        };
      }
    } catch (err) {
      console.error("Auth failed → redirecting to login");
      navigate("/login");
    }
  };

    connect();
    
    return () => {
      if (ws && ws.readyState === WebSocket.OPEN) {
        ws.close();
      }
    }


}, [chatId]);


  

  const handleSend = () => {
    if (!newMessage.trim()) return;

  



    setMessages(prev => [
      ...prev,
      {
        id: Date.now(),
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
        {messages.map((msg) => (
          <div
            key={msg.id}
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
