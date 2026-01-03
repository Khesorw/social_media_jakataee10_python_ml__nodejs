import { useNavigate } from "react-router-dom";
import React, { useEffect, useState } from "react";
import "../index.css";
import axios from "axios";

// const chats = [
//   {
//     chatId: "2",
//     user: { id: "3", name: "Diana" },
//     lastMessage: " donkey kong",
//     timestamp: "11:05",
//     unreadCount: 5,
//   },
// ];

export default function Feed() {
  const [isSearching, setIsSearching] = useState(false);
  const [query, setQuery] = useState("");
  const navigate = useNavigate();
  const [chats, setChats] = useState([]);
  const [myUserId, setMyUserId] = useState(null);




  useEffect(() => {

    const fetchUserId = async () => {
      
      try {
        const res = await axios.get('/corechat/core/me');
        console.log("printing user id "+res.data.id);

        setMyUserId(res.data.id);

      } catch (error) {
        console.log(error);
        
        } 
    }

    fetchUserId();

    return () => {
      console.log("testing cleanup");
    }
      

  }, []);


  useEffect(() => {
    const feedHistory = async () => {


      console.log("before feed history get request ");
      const response = await axios.get('/corechat/core/feedHistory');
      console.log("printing return from feed response: " + response.data);

      if (response.status == 200) {
        setChats(response.data)
        
      } else {
        console.log("issue with response " + response.status)
        return;
      }
      
    }

    feedHistory();

    
  }, []);



  const formateMessageDate = d => {
    
    if (!d) return "";
    const date = new Date(d);

    return date.toLocaleString('en-US', {
      month: 'short',
      day: 'numeric',
      hour: 'numeric',
      minute: '2-digit',
    });

  };



  // This handles both the Enter key and the Search button click
  const handleSubmit = (e) => {
    e.preventDefault();
    if (!query.trim()) return;
  };

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      {/* Header */}
      <header className="bg-white border-b px-4 py-3 flex items-center justify-between">
        <h1 className="text-lg font-semibold text-gray-900">Chats</h1>

        <div className="flex items-center gap-4 text-gray-600">
          {isSearching ? (
            <form onSubmit={handleSubmit} className="flex items-center gap-2">
              <input
                autoFocus
                type="text"
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                placeholder="Search user..."
                className="border rounded-md px-2 py-1 text-sm focus:outline-indigo-500"
              />
              <button type="submit" className="text-sm bg-indigo-600 text-white px-2 py-1 rounded">
                Enter
              </button>
              <button
                type="button"
                onClick={() => setIsSearching(false)}
                className="text-xs text-gray-400"
              >
                Cancel
              </button>
            </form>
          ) : (
            <>
              <button className="text-xl" onClick={() => setIsSearching(true)}>🔍</button>
              <button className="text-xl">➕</button>
            </>
          )}
        </div>
      </header>

      {/* Global Search (Optional - keeps UI consistent) */}
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
            key={chat.conversationId}
            onClick={() => navigate(`/chat/${chat.conversationId}`)}
            className="flex items-center gap-3 px-4 py-3 bg-white border-b cursor-pointer hover:bg-gray-100"
          >
            <div className="w-12 h-12 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-600 font-semibold">
              {
                chat.otherUserName.length > 1 ?
                  chat.otherUserName.charAt(0) + chat.otherUserName.charAt(1) :
                  chat.otherUserName.charAt(0)
              }
            </div>

            <div className="flex-1 min-w-0">
              <div className="flex justify-between items-center">
                <h2 className="font-medium text-gray-900 truncate">
                  {chat.otherUserName}
                </h2>
                <span className="text-xs text-gray-400">
                  {formateMessageDate(chat.createdAt)}
                </span>
              </div>
              <div className="flex justify-between items-center mt-1">
                <p className="text-sm text-gray-500 truncate">
                  {chat.lastMessageText || "No messages yet"}
                </p>
                {/* {chat.unreadCount > 0 && (
                  <span className="ml-2 min-w-[20px] h-5 px-1 text-xs bg-red-600 text-white rounded-full flex items-center justify-center">
                    {chat.unreadCount}
                  </span>
                )} */}
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
