import { BrowserRouter, Routes, Route } from "react-router-dom";

import Feed from "./pages/Feed";
import Chat from "./pages/Chat";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Welcome from "./pages/Welcome";

function App() {
  return (
    <BrowserRouter basename="/corechat">
      <Routes>
        <Route path="/" element={<Welcome />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/feed" element={<Feed />} />
        <Route path="/chat/:chatId" element={<Chat />} />
      </Routes>
    </BrowserRouter>

  );
}

export default App;
