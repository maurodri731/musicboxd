import { useEffect, useState } from 'react'


const TopArtists = () => {

    const [userTopArtists, setUserTopArtists] = useState<any[]>([]);
    useEffect(() => {
        fetch("http://localhost:8080/api/user-top-artists")
        .then(response => response.json())
        .then(data => {
            console.log(data)
            setUserTopArtists(data)
        })
    });
  return (
    <div>
        {userTopArtists ? (
            userTopArtists.map((artistResult) => {
                return <h1 className="text" key= {artistResult.name}>{artistResult.name}</h1>
            })
        ):
        (
            <h1>LOADING...</h1>
        )}
    </div>

  );
}

export default TopArtists