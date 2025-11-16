import React, { useState, useEffect } from 'react';
import PlayersTab from './components/PlayersTab';
import MatchesTab from './components/MatchesTab';
import StatsTab from './components/StatsTab';
import LoginPage from './components/LoginPage';
import SplineBackground from './components/SplineBackground';
import './App.css';

function App() {
  const [activeTab, setActiveTab] = useState('players');
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState(null);

  useEffect(() => {
    // Check if user is already logged in
    const authStatus = localStorage.getItem('isAuthenticated');
    const userData = localStorage.getItem('user');
    
    if (authStatus === 'true' && userData) {
      setIsAuthenticated(true);
      setUser(JSON.parse(userData));
    }
  }, []);

  const handleLoginSuccess = (userData) => {
    setUser(userData);
    setIsAuthenticated(true);
  };

  const handleLogout = () => {
    localStorage.removeItem('isAuthenticated');
    localStorage.removeItem('user');
    setIsAuthenticated(false);
    setUser(null);
  };

  // Show login page if not authenticated
  if (!isAuthenticated) {
    return <LoginPage onLoginSuccess={handleLoginSuccess} />;
  }

  const appStyle = {
    minHeight: '100vh',
    display: 'flex',
    flexDirection: 'column',
    position: 'relative',
    zIndex: 1,
    backgroundColor: 'transparent'
  };

  return (
    <div className="App" style={appStyle}>
      <SplineBackground />
      <header className="header">
        <div className="header-content">
          <div>
            <h1>ROLLINS TENNIS ARCHIVE</h1>
            <p>Match Results & Player Statistics</p>
          </div>
          <div className="user-info">
            {user && (
              <>
                <img 
                  src={user.picture} 
                  alt={user.name} 
                  className="user-avatar"
                />
                <span className="user-name">{user.name}</span>
                <button onClick={handleLogout} className="logout-button">
                  Logout
                </button>
              </>
            )}
          </div>
        </div>
      </header>

      <nav className="navbar">
        <button 
          className={activeTab === 'players' ? 'active' : ''}
          onClick={() => setActiveTab('players')}
        >
          Players
        </button>
        <button 
          className={activeTab === 'matches' ? 'active' : ''}
          onClick={() => setActiveTab('matches')}
        >
          Matches
        </button>
        <button 
          className={activeTab === 'stats' ? 'active' : ''}
          onClick={() => setActiveTab('stats')}
        >
          Statistics
        </button>
      </nav>

      <main className="content">
        {activeTab === 'players' && <PlayersTab />}
        {activeTab === 'matches' && <MatchesTab />}
        {activeTab === 'stats' && <StatsTab />}
      </main>

      <footer className="footer">
        <p>Rollins College Tennis | 2022-2024</p>
      </footer>
    </div>
  );
}

export default App;

