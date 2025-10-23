import { Routes, Route, useLocation } from "react-router-dom";
import Login from "./components/Login";
import TopArtists from "./components/TopArtists";
import 'bootstrap/dist/css/bootstrap.min.css';
import UserPage from "./components/UserPage";
import Landing from "./pages/Landing";
import AuthPage from "./pages/AuthPage";
import { AnimatePresence } from 'framer-motion';
import SearchAlbums from "./pages/SearchAlbums";
import BackgroundGradient from "./components/BackgroundGradient";


const App = () => {
  const location = useLocation();
  return (
      <div>
      <BackgroundGradient gradient="linear-gradient(135deg, #581c87 0%, #000 50%, #000 100%)"/>
      <AnimatePresence mode="wait" initial={false}>
          <Routes location={location} key={location.pathname}>
            <Route path="/" element={<div><Landing /></div>} />
            <Route path="/auth" element={<div><AuthPage /></div>} />
            <Route path="/log-in" element={<div><Login/></div>}/>
            <Route path="/user-top-artists" element={<div><TopArtists/></div>}/> 
            <Route path="/user-page" element={<div><UserPage/></div>}/>
            <Route path="/search-albums" element={<div><SearchAlbums/></div>}/>
            </Routes>
        </AnimatePresence>
      </div>
  )
}
export default App;