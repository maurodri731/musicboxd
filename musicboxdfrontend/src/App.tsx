import { Routes, Route } from "react-router-dom";
import Login from "./components/Login";
import TopArtists from "./components/TopArtists";
import 'bootstrap/dist/css/bootstrap.min.css';

const App = () => {
  return (
      <div>
        <Routes>
          <Route path="/" element={<Login/>}/>
          <Route path="/user-top-artists" element={<TopArtists/>}/> 
        </Routes>
      </div>
  )
}
export default App;