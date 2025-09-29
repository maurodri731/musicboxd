const getSpotifyUserLogin = () => {
  fetch("http://localhost:8080/api/spotify-login")
  .then((response) => response.text())
  .then(response => {
    window.location.replace(response);
  })
}
const SocialLogin = () => {
    return (
      <div className="social-login">
        <button className="social-button" onClick={() => getSpotifyUserLogin()}>
          <img src="spotify.svg" alt="Spotify" className="social-icon" />
          Spotify
        </button>
      </div>
    )
  }
  export default SocialLogin;