import React, { useState, useEffect } from 'react';
import { api } from '../services/api';

function PlayersTab() {
  const [players, setPlayers] = useState([]);
  const [selectedPlayer, setSelectedPlayer] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadPlayers();
  }, []);

  const loadPlayers = async () => {
    try {
      const data = await api.getPlayers();
      setPlayers(data);
      setLoading(false);
    } catch (error) {
      console.error('Error loading players:', error);
      setLoading(false);
    }
  };

  const filteredPlayers = players.filter(player => {
    const fullName = `${player.firstName} ${player.lastName}`.toLowerCase();
    return fullName.includes(searchTerm.toLowerCase());
  });

  const handlePlayerClick = async (player) => {
    setSelectedPlayer(player);
    try {
      const stats = await api.getPlayerStats(player.id);
      setSelectedPlayer({ ...player, stats });
    } catch (error) {
      console.error('Error loading player stats:', error);
    }
  };

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  return (
    <div className="players-container">
      <div className="players-list">
        <div className="search-box">
          <input
            type="text"
            placeholder="Search players..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
        <h3>Players ({filteredPlayers.length})</h3>
        {filteredPlayers.map(player => (
          <div
            key={player.id}
            className={`player-item ${selectedPlayer?.id === player.id ? 'selected' : ''}`}
            onClick={() => handlePlayerClick(player)}
            style={{ display: 'flex', alignItems: 'center', gap: '10px' }}
          >
            {player.image && (
              <img 
                src={player.image} 
                alt={player.firstName}
                style={{ width: '50px', height: '50px', objectFit: 'cover', borderRadius: '50%' }}
              />
            )}
            <div>
              <div className="player-name">{player.firstName} {player.lastName}</div>
              <div className="player-info">{player.classYear} • UTR {player.utr}</div>
            </div>
          </div>
        ))}
      </div>

      <div className="player-details">
        {selectedPlayer ? (
          <>
            <h3>Player Details</h3>
            {selectedPlayer.image && (
              <div style={{ textAlign: 'center', marginBottom: '20px' }}>
                <img 
                  src={selectedPlayer.image} 
                  alt={selectedPlayer.firstName}
                  style={{ width: '150px', height: '150px', objectFit: 'cover', borderRadius: '50%' }}
                />
              </div>
            )}
            <div className="detail-row">
              <div className="detail-label">Name</div>
              <div className="detail-value">{selectedPlayer.firstName} {selectedPlayer.lastName}</div>
            </div>
            <div className="detail-row">
              <div className="detail-label">Class Year</div>
              <div className="detail-value">{selectedPlayer.classYear}</div>
            </div>
            <div className="detail-row">
              <div className="detail-label">Nationality</div>
              <div className="detail-value">{selectedPlayer.nationality}</div>
            </div>
            <div className="detail-row">
              <div className="detail-label">UTR Rating</div>
              <div className="detail-value">{selectedPlayer.utr}</div>
            </div>
            
            {selectedPlayer.stats && (
              <div className="stats-grid">
                <div className="stat-box">
                  <div className="stat-value">{selectedPlayer.stats.wins}</div>
                  <div className="stat-label">Wins</div>
                </div>
                <div className="stat-box">
                  <div className="stat-value">{selectedPlayer.stats.losses}</div>
                  <div className="stat-label">Losses</div>
                </div>
                <div className="stat-box">
                  <div className="stat-value">{selectedPlayer.stats.winPercentage.toFixed(1)}%</div>
                  <div className="stat-label">Win %</div>
                </div>
              </div>
            )}
          </>
        ) : (
          <div style={{ color: '#666', textAlign: 'center', marginTop: '50%' }}>
            Select a player to view details
          </div>
        )}
      </div>
    </div>
  );
}

export default PlayersTab;

