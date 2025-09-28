import { Routes, Route } from "react-router-dom";
import Login from "./components/Login";
import TopArtists from "./components/TopArtists";
import 'bootstrap/dist/css/bootstrap.min.css';
import UserPage from "./components/UserPage";

const App = () => {
  return (
      <div>
        <Routes>
          <Route path="/" element={<Login/>}/>
          <Route path="/user-top-artists" element={<TopArtists/>}/> 
          <Route path="/user-page" element={<UserPage/>}/>
        </Routes>
      </div>
  )
}
export default App;