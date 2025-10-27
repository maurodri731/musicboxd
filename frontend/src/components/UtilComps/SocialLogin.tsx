const getSpotifyUserLogin = () => {
  fetch("http://localhost:8080/auth/spotify-login")
    .then((response) => response.text())
    .then(response => {
      window.location.replace(response);
    });
};

const SocialLogin = () => {
  return (
    <div className="flex gap-5">
      <button 
        className="flex items-center justify-center gap-3 w-full text-base font-medium px-4 py-3 rounded bg-purple-50 border border-purple-200 hover:border-indigo-900 hover:bg-purple-100 transition-all"
        onClick={() => getSpotifyUserLogin()}
      >
        <img src="spotify.svg" alt="Spotify" className="w-6" />
        Spotify
      </button>
    </div>
  );
};

export default SocialLogin;