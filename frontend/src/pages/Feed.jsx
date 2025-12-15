import { useNavigate } from "react-router-dom";
import React from "react";
import "../index.css";

const chats = [
  {
    chatId: "1",
    user: { id: "101", name: "Alice" },
    lastMessage: "Hey, are you free today?",
    timestamp: "14:32",
    unreadCount: 2,
  },
  {
    chatId: "2",
    user: { id: "102", name: "Bob" },
    lastMessage: "Let’s meet tomorrow",
    timestamp: "13:10",
    unreadCount: 0,
  },
  {
    chatId: "3",
    user: { id: "103", name: "Charlie" },
    lastMessage: "👍",
    timestamp: "11:05",
    unreadCount: 0,
  },
];

export default function Feed() {

    console.log("Rendering Feed component");
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">

      {/* Header */}
      <header className="bg-white border-b px-4 py-3 flex items-center justify-between">
        <h1 className="text-lg font-semibold text-gray-900">Chats</h1>
        <div className="flex gap-4 text-gray-600">
          <button className="text-xl">🔍</button>
          <button className="text-xl">➕</button>
        </div>
      </header>

      {/* Search */}
      <div className="p-3 bg-white border-b">
        <input
          type="text"
          placeholder="Search conversations"
          className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-200"
        />
      </div>

      {/* Chat List */}
      <div className="flex-1 overflow-y-auto">
        {chats.map((chat) => (
          <div
            key={chat.chatId}
            onClick={() => navigate(`/chat/${chat.chatId}`)}
            className="flex items-center gap-3 px-4 py-3 bg-white border-b cursor-pointer hover:bg-gray-100"
          >
            {/* Avatar */}
            <div className="w-12 h-12 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-600 font-semibold">
              {chat.user.name.charAt(0)}
            </div>

            {/* Chat Info */}
            <div className="flex-1 min-w-0">
              <div className="flex justify-between items-center">
                <h2 className="font-medium text-gray-900 truncate">
                  {chat.user.name}
                </h2>
                <span className="text-xs text-gray-400">
                  {chat.timestamp}
                </span>
              </div>
              <div className="flex justify-between items-center mt-1">
                <p className="text-sm text-gray-500 truncate">
                  {chat.lastMessage}
                </p>
                {chat.unreadCount > 0 && (
                  <span className="ml-2 min-w-[20px] h-5 px-1 text-xs bg-indigo-600 text-white rounded-full flex items-center justify-center">
                    {chat.unreadCount}
                  </span>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
