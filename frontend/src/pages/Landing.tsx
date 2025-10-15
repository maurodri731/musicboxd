import { Heart, Users, TrendingUp, Disc3, CassetteTape } from 'lucide-react';
import "../style/Landing.css";
import { Link } from 'react-router-dom';
import NavbarComp from '../components/NavbarComp';

export default function Landing() {
  console.log("Landing page rendering")
  return (
    <>
      <NavbarComp/>
      <div className="content-wrapper">
        <div className="container py-5">
          <div className="row">
            <div className="col-lg-8">
              <div className="badge-custom">
                ✨ Join music enthusiasts
              </div>
              <h1 className="hero-title">
                Collect Your Musical Journey
              </h1>
              <p className="hero-description">
                Rate albums, create lists, and share your music taste with a community that truly gets it. Your personal music diary starts here.
              </p>
              <div className="d-flex gap-3 mb-5 flex-wrap">
                <button className="btn btn-primary-custom">
                  <Heart size={20} />
                  Start Collection
                </button>
                <button className="btn btn-secondary-custom">
                  Browse Albums
                </button>
              </div>

              <div className="row g-3 mb-5">
                {[1, 2, 3, 4].map((i) => (
                  <div key={i} className="col-6 col-md-3">
                    <div className="album-placeholder"></div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>

        <div className="container pb-5">
          <div className="row g-4">
            <div className="col-md-4">
              <div className="feature-card">
                <Disc3 className="feature-icon icon-purple" />
                <h3 className="feature-title">Unlimited Reviews</h3>
                <p className="feature-description">
                  Write as many reviews as you want. No limits on your expression.
                </p>
              </div>
            </div>
            <div className="col-md-4">
              <div className="feature-card">
                <Users className="feature-icon icon-pink" />
                <h3 className="feature-title">Follow Friends</h3>
                <p className="feature-description">
                  See what your friends are listening to and discover new music.
                </p>
              </div>
            </div>
            <div className="col-md-4">
              <div className="feature-card">
                <TrendingUp className="feature-icon icon-blue" />
                <h3 className="feature-title">Trending Albums</h3>
                <p className="feature-description">
                  Stay updated with the most talked about releases.
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}