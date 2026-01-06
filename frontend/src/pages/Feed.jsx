import { useNavigate } from "react-router-dom";
import React, { useEffect, useState } from "react";
import "../index.css";
import axios from "axios";

export default function Feed() {
  const [isSearching, setIsSearching] = useState(false);
  const [query, setQuery] = useState("");
  const navigate = useNavigate();
  const [chats, setChats] = useState([]);
  const [searchResult, setSearchResult] = useState([]);



  // Fetch Conversation History
  useEffect(() => {
    const feedHistory = async () => {
      try {
        const response = await axios.get('/corechat/core/feedHistory');
        if (response.status === 200) setChats(response.data);
      } catch (error) {
        console.error("History fetch error", error);
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

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!query.trim()) return;

    try {
      const response = await axios.get('/corechat/core/feed/search', {
        params: { input: query }
      });
      // The backend returns an array of UserDto objects
      setSearchResult(response.data);
    } catch (error) {
      console.error("Search error", error);
    }
  };

  const handleCloseSearch = () => {
    setIsSearching(false);
    setSearchResult([]); // Clear results when closing
    setQuery("");        // Clear input
  };



  const handleAddFriend = async (user) => {
    console.log("Future logic: Add friend/Start chat with", user.id);
    try {
      const response = await axios.post('/corechat/core/create/convroom', {
        otherUserid: user.id
      });

      console.log("conversation room created convId is: " + response.data.convId);
      navigate(`/chat/${response.data.conversationId}`);
      
    } catch (error) {

      console.log("error while creating new conv " + error);
      
    }
  };

  const handleGoToConversation = (convId) => {
  navigate(`/chat/${convId}`);
};


  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      {/* Header */}
      <header className="bg-white border-b px-4 py-3 flex items-center justify-between shadow-sm">
        <h1 className="text-lg font-semibold text-gray-900">
          {isSearching ? "Find Friends" : "Chats"}
        </h1>

        <div className="flex items-center gap-4 text-gray-600">
          {isSearching ? (
            <form onSubmit={handleSubmit} className="flex items-center gap-2">
              <input
                autoFocus
                type="text"
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                placeholder="Name or email..."
                className="border rounded-md px-2 py-1 text-sm focus:outline-indigo-500 w-40"
              />
              <button
                type="button"
                onClick={handleCloseSearch}
                className="text-sm font-medium text-indigo-600 hover:text-indigo-800"
              >
                Done
              </button>
            </form>
          ) : (
            <>
              <button className="text-xl hover:scale-110 transition" onClick={() => setIsSearching(true)}>🔍</button>
            </>
          )}
        </div>
      </header>

      {/* Conditional Search Results View */}
      {isSearching ? (
        <div className="flex-1 bg-white overflow-y-auto">
          <div className="p-2 bg-gray-50 text-xs font-bold text-gray-500 uppercase tracking-wider">
            Search Results ({searchResult.length})
          </div>
          {searchResult.length > 0 ? (
            searchResult.map((user) => (
              <div key={user.id} className="flex items-center justify-between px-4 py-3 border-b hover:bg-indigo-50 transition">
                <div className="flex items-center gap-3">
                  <div className="w-10 h-10 rounded-full bg-indigo-500 flex items-center justify-center text-white font-bold">
                    {user.username.charAt(0).toUpperCase()}
                  </div>
                  <div>
                    <h3 className="text-sm font-semibold text-gray-900">{user.username}</h3>
                    <p className="text-xs text-gray-500">{user.email}</p>
                  </div>
                </div>
                {!user.existing ? (
                    <button
                      onClick={() => handleAddFriend(user)}
                      className="w-8 h-8 flex items-center justify-center bg-indigo-100 text-indigo-600 rounded-full hover:bg-indigo-600 hover:text-white transition"
                    >
                      ＋
                    </button>
                  ) : (
                    <button
                      onClick={() => handleGoToConversation(user.convId)}
                      className="text-xs px-3 py-1 rounded-full bg-gray-200 text-gray-700 hover:bg-indigo-600 hover:text-white transition"
                    >
                      Go to chat
                    </button>
                  )}
              </div>
            ))
          ) : (
            <div className="p-10 text-center text-gray-400 italic">
              Search for a user by name or email...
            </div>
          )}
        </div>
      ) : (
        /* Regular Conversation List */
        <div className="flex-1 overflow-y-auto">
          {chats.map((chat) => (
            <div
              key={chat.conversationId}
              onClick={() => navigate(`/chat/${chat.conversationId}`)}
              className="flex items-center gap-3 px-4 py-3 bg-white border-b cursor-pointer hover:bg-gray-100"
            >
              <div className="w-12 h-12 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-600 font-semibold">
                {chat.otherUserName.slice(0, 2).toUpperCase()}
              </div>

              <div className="flex-1 min-w-0">
                <div className="flex justify-between items-center">
                  <h2 className="font-medium text-gray-900 truncate">{chat.otherUserName}</h2>
                  <span className="text-xs text-gray-400">{formateMessageDate(chat.createdAt)}</span>
                </div>
                <p className="text-sm text-gray-500 truncate mt-1">
                  {chat.lastMessageText || "No messages yet"}
                </p>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
