import { Navbar, Container, Nav, NavDropdown } from "react-bootstrap";
import { CassetteTape } from "lucide-react";
import { Link, useLocation } from "react-router-dom";

//REMEMBER TO GET THE LOGO OUT OF THE AUTHPAGE!!!!!!! IT SHOULD BE PART OF THIS COMPONENT!!!!!!!!
export default function NavbarComp() {
    const location = useLocation();
    const getNavMode = () => {
      if(location.pathname === '/') return 'landing';
      if(location.pathname === '/auth') return 'auth';
      if(location.pathname === '/user-page') return 'user-page';
    }
    const mode = getNavMode();

    return (
      <div>
        <Navbar
          fixed="top"
          data-bs-theme="dark"
          expand="lg"
          bg="transparent"
        >
          <Container className="px-0">
            <Navbar.Brand href="/">
              <div className="d-flex align-items-center">
                <svg width="0" height="0">
                  <defs>
                    <linearGradient id="myGradient" x1="0%" y1="0%" x2="100%" y2="100%">
                      <stop offset="0%" stopColor="#9333ea" />
                      <stop offset="100%" stopColor="#ec4899" />
                    </linearGradient>
                  </defs>
                </svg>
                <CassetteTape size={40} stroke="url(#myGradient)"/>
                <span className="brand-text" style={mode === 'auth' ? { color: "#000"} : { color: "#fff"}}>tapelog</span>
              </div>
            </Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Collapse id="basic-navbar-nav">
              { mode === 'user-page' && (
              <Nav className="ms-auto">
                <Nav.Link href="#home">Home</Nav.Link>
                <Nav.Link href="#link">Link</Nav.Link>
                <NavDropdown title="Dropdown" id="basic-nav-dropdown">
                  <NavDropdown.Item href="#action/3.1">Action</NavDropdown.Item>
                  <NavDropdown.Item href="#action/3.2">
                    Another action
                  </NavDropdown.Item>
                  <NavDropdown.Item href="#action/3.3">
                    Something
                  </NavDropdown.Item>
                  <NavDropdown.Divider />
                  <NavDropdown.Item href="#action/3.4">
                    Separated link
                  </NavDropdown.Item>
                </NavDropdown>
              </Nav>)}
              { mode === 'landing' && (
              <Nav className="ms-auto" style={{padding: "0 0 10px 0"}}>
                <div className="d-flex gap-3">
                  <Link to='/auth?mode=sign-in'>
                    <button className="btn btn-outline-custom">Sign In</button>
                  </Link>
                  <Link to='/auth?mode=sign-up'>
                    <button className="btn btn-gradient">Sign Up</button>
                  </Link>
                </div>
              </Nav>)}
            </Navbar.Collapse>
          </Container>
        </Navbar>
      </div>
    );
  }
