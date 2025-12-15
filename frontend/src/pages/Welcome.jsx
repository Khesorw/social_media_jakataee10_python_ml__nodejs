import { Link } from "react-router-dom";

export default function Welcome() {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-pink-400 via-indigo-400 to-cyan-400 relative overflow-hidden">

      {/* Floating bubble effects */}
      <div className="absolute inset-0">
        <div className="absolute top-10 left-10 w-32 h-32 bg-white/20 rounded-full blur-xl animate-float" />
        <div className="absolute bottom-20 right-12 w-40 h-40 bg-white/20 rounded-full blur-xl animate-float delay-200" />
        <div className="absolute top-1/3 right-1/4 w-24 h-24 bg-white/20 rounded-full blur-xl animate-float delay-500" />
      </div>

      {/* Card */}
      <div className="relative z-10 w-full max-w-sm mx-4 bg-white/80 backdrop-blur-xl rounded-3xl shadow-2xl px-8 py-10 text-center">

        {/* Logo */}
        <div className="flex justify-center mb-6">
            <div className="relative flex justify-center mb-8">
            <div className="bubble-logo animate-heartbeat">
                <span className="bubble-text">Vibin</span>
            </div>
            </div>

        
        </div>

        {/* Title */}
        <h1 className="text-3xl font-extrabold text-gray-900 mb-2">
          Welcome to <span className="text-transparent bg-clip-text bg-gradient-to-r from-pink-500 via-purple-500 to-indigo-500">Vibin</span>
        </h1>

        <p className="text-gray-600 text-sm mb-8">
          A modern chat app to connect, vibe, and stay close 💬✨
        </p>

        {/* Actions */}
        <div className="flex flex-col gap-4">
          <Link
            to="/login"
            className="w-full py-3 rounded-full bg-gradient-to-r from-indigo-500 to-purple-500 text-white font-semibold shadow-md hover:scale-[1.02] transition"
          >
            Login
          </Link>

          <Link
            to="/register"
            className="relative overflow-hidden register-glow-btn"
            >
            Register
         </Link>


   
        </div>

        {/* Footer */}
        <p className="mt-8 text-xs text-gray-500">
          Vibin © {new Date().getFullYear()}
        </p>
      </div>
    </div>
  );
}
