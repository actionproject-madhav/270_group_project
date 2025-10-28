import React from 'react';
import PlayersTab from './components/PlayersTab';
import MatchesTab from './components/MatchesTab';
import StatsTab from './components/StatsTab';
import './App.css';

function App() {
  const [activeTab, setActiveTab] = React.useState('players');

  const appStyle = {
    minHeight: '100vh',
    display: 'flex',
    flexDirection: 'column',
    backgroundImage: "url('/images/tennis.jpg')",
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    backgroundAttachment: 'fixed',
    backgroundRepeat: 'no-repeat'
  };

  return (
    <div className="App" style={appStyle}>
      <header className="header">
        <h1>ROLLINS TENNIS ARCHIVE</h1>
        <p>Match Results & Player Statistics</p>
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

