import React, { useState, useEffect } from 'react';
import { api } from '../services/api';

function PlayersTab() {
  const [players, setPlayers] = useState([]);
  const [selectedPlayer, setSelectedPlayer] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);
  const [loadingStats, setLoadingStats] = useState(false);
  const [sortBy, setSortBy] = useState('name');
  const [sortOrder, setSortOrder] = useState('asc');

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
    const searchFields = [
      fullName,
      player.nationality?.toLowerCase() || '',
      player.classYear?.toLowerCase() || '',
      player.utr?.toString() || ''
    ];
    return searchFields.some(field => field.includes(searchTerm.toLowerCase()));
  }).sort((a, b) => {
    let aValue, bValue;
    switch (sortBy) {
      case 'name':
        aValue = `${a.firstName} ${a.lastName}`;
        bValue = `${b.firstName} ${b.lastName}`;
        break;
      case 'year':
        aValue = a.classYear;
        bValue = b.classYear;
        break;
      case 'utr':
        aValue = parseFloat(a.utr) || 0;
        bValue = parseFloat(b.utr) || 0;
        break;
      default:
        return 0;
    }
    
    if (sortOrder === 'asc') {
      return aValue > bValue ? 1 : -1;
    } else {
      return aValue < bValue ? 1 : -1;
    }
  });

  const handlePlayerClick = async (player) => {
    if (selectedPlayer?.id === player.id) return;
    
    setSelectedPlayer(player);
    setLoadingStats(true);
    
    try {
      const stats = await api.getPlayerStats(player.id);
      setSelectedPlayer({ ...player, stats });
    } catch (error) {
      console.error('Error loading player stats:', error);
      setSelectedPlayer({ ...player, stats: null });
    } finally {
      setLoadingStats(false);
    }
  };

  const clearSearch = () => {
    setSearchTerm('');
  };

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  return (
    <div className="players-container">
      <div className="players-list">
        <div className="search-box">
          <div style={{ position: 'relative' }}>
          <input
            type="text"
              placeholder="Search by name, class, nationality, or UTR..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
              style={{ paddingRight: searchTerm ? '40px' : '15px' }}
            />
            {searchTerm && (
              <button
                onClick={clearSearch}
                style={{
                  position: 'absolute',
                  right: '10px',
                  top: '50%',
                  transform: 'translateY(-50%)',
                  background: 'none',
                  border: 'none',
                  color: '#888',
                  cursor: 'pointer',
                  fontSize: '16px',
                  padding: '0',
                  width: '20px',
                  height: '20px',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center'
                }}
              >
                ×
              </button>
            )}
          </div>
          <div style={{ 
            display: 'flex', 
            gap: '10px', 
            marginBottom: '15px',
            flexWrap: 'wrap',
            alignItems: 'center'
          }}>
            <select
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value)}
              style={{
                background: 'rgba(0, 0, 0, 0.5)',
                color: '#fff',
                border: '1px solid rgba(255, 255, 255, 0.2)',
                borderRadius: '6px',
                padding: '6px 10px',
                fontSize: '12px'
              }}
            >
              <option value="name">Sort by Name</option>
              <option value="year">Sort by Year</option>
              <option value="utr">Sort by UTR</option>
            </select>
            <button
              onClick={() => setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc')}
              style={{
                background: 'rgba(255, 167, 38, 0.2)',
                color: '#FFA726',
                border: '1px solid rgba(255, 167, 38, 0.3)',
                borderRadius: '6px',
                padding: '6px 10px',
                fontSize: '12px',
                cursor: 'pointer',
                transition: 'all 0.3s ease'
              }}
            >
              {sortOrder === 'asc' ? '↑' : '↓'}
            </button>
          </div>
        </div>
        
        <h3>Players ({filteredPlayers.length})</h3>
        
        <div style={{ maxHeight: '500px', overflowY: 'auto' }}>
          {filteredPlayers.length === 0 ? (
            <div style={{ 
              textAlign: 'center', 
              padding: '40px 20px', 
              color: '#666',
              fontSize: '14px'
            }}>
              {searchTerm ? `No players found matching "${searchTerm}"` : 'No players available'}
            </div>
          ) : (
            filteredPlayers.map((player, index) => (
          <div
            key={player.id}
            className={`player-item ${selectedPlayer?.id === player.id ? 'selected' : ''}`}
            onClick={() => handlePlayerClick(player)}
                style={{ 
                  display: 'flex', 
                  alignItems: 'center', 
                  gap: '15px',
                  animationDelay: `${index * 0.1}s`
                }}
              >
                <div style={{
                  width: '60px',
                  height: '60px',
                  borderRadius: '50%',
                  background: player.image 
                    ? `url(${player.image})` 
                    : 'linear-gradient(135deg, #FFA726, #FFB74D)',
                  backgroundSize: 'cover',
                  backgroundPosition: 'center',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  color: '#fff',
                  fontWeight: 'bold',
                  fontSize: '18px',
                  border: '2px solid rgba(255, 167, 38, 0.4)',
                  flexShrink: 0,
                  overflow: 'hidden'
                }}>
                  {!player.image && `${player.firstName[0]}${player.lastName[0]}`}
                </div>
                <div style={{ flex: 1, minWidth: 0 }}>
              <div className="player-name">{player.firstName} {player.lastName}</div>
                  <div className="player-info">
                    <span>{player.classYear}</span>
                    <span>•</span>
                    <span>{player.nationality}</span>
                    <span>•</span>
                    <span>UTR {player.utr}</span>
                  </div>
                </div>
                <div style={{
                  fontSize: '18px',
                  color: selectedPlayer?.id === player.id ? '#FFA726' : '#666',
                  transition: 'color 0.3s ease'
                }}>
                  →
            </div>
          </div>
            ))
          )}
        </div>
      </div>

      <div className="player-details">
        {selectedPlayer ? (
          <div className="fade-in">
            <h3>Player Profile</h3>
            
            <div style={{ textAlign: 'center', marginBottom: '25px' }}>
              <div style={{
                width: '120px',
                height: '120px',
                borderRadius: '50%',
                background: selectedPlayer.image 
                  ? `url(${selectedPlayer.image})` 
                  : 'linear-gradient(135deg, #FFA726, #FFB74D)',
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                color: '#fff',
                fontWeight: 'bold',
                fontSize: '32px',
                border: '3px solid rgba(255, 167, 38, 0.5)',
                margin: '0 auto',
                boxShadow: '0 8px 25px rgba(255, 167, 38, 0.3)',
                transition: 'all 0.3s ease',
                overflow: 'hidden'
              }}>
                {!selectedPlayer.image && `${selectedPlayer.firstName[0]}${selectedPlayer.lastName[0]}`}
              </div>
            </div>

            <div className="detail-row">
              <div className="detail-label">Full Name</div>
              <div className="detail-value">{selectedPlayer.firstName} {selectedPlayer.lastName}</div>
            </div>
            
            <div className="detail-row">
              <div className="detail-label">Class Year</div>
              <div className="detail-value">
                <span style={{
                  background: 'rgba(255, 167, 38, 0.2)',
                  color: '#FFA726',
                  padding: '4px 8px',
                  borderRadius: '4px',
                  fontSize: '14px',
                  fontWeight: '600'
                }}>
                  {selectedPlayer.classYear}
                </span>
              </div>
            </div>
            
            <div className="detail-row">
              <div className="detail-label">Nationality</div>
              <div className="detail-value">
                <span style={{ fontSize: '18px', marginRight: '8px' }}>
                  {selectedPlayer.nationality === 'USA' ? '🇺🇸' : 
                   selectedPlayer.nationality === 'Spain' ? '🇪🇸' :
                   selectedPlayer.nationality === 'Germany' ? '🇩🇪' :
                   selectedPlayer.nationality === 'France' ? '🇫🇷' : '🌍'}
                </span>
                {selectedPlayer.nationality}
              </div>
            </div>
            
            <div className="detail-row">
              <div className="detail-label">UTR Rating</div>
              <div className="detail-value">
                <span style={{
                  background: 'linear-gradient(135deg, #FFA726, #FFB74D)',
                  color: '#fff',
                  padding: '6px 12px',
                  borderRadius: '8px',
                  fontSize: '16px',
                  fontWeight: '700',
                  boxShadow: '0 4px 12px rgba(255, 167, 38, 0.3)'
                }}>
                  {selectedPlayer.utr}
                </span>
              </div>
            </div>
            
            {loadingStats ? (
              <div style={{ 
                textAlign: 'center', 
                padding: '30px',
                color: '#888',
                fontSize: '14px'
              }}>
                <div style={{
                  width: '30px',
                  height: '30px',
                  border: '3px solid rgba(255, 167, 38, 0.3)',
                  borderTop: '3px solid #FFA726',
                  borderRadius: '50%',
                  animation: 'spin 1s linear infinite',
                  margin: '0 auto 15px'
                }}></div>
                Loading statistics...
              </div>
            ) : selectedPlayer.stats ? (
              <div className="stats-grid">
                <div className="stat-box">
                  <div className="stat-value">{selectedPlayer.stats.wins}</div>
                  <div className="stat-label">Career Wins</div>
                </div>
                <div className="stat-box">
                  <div className="stat-value">{selectedPlayer.stats.losses}</div>
                  <div className="stat-label">Career Losses</div>
                </div>
                <div className="stat-box">
                  <div className="stat-value">{selectedPlayer.stats.winPercentage?.toFixed(1) || 0}%</div>
                  <div className="stat-label">Win Percentage</div>
                </div>
              </div>
            ) : (
              <div style={{
                textAlign: 'center',
                padding: '20px',
                background: 'rgba(255, 193, 7, 0.1)',
                border: '1px solid rgba(255, 193, 7, 0.3)',
                borderRadius: '8px',
                color: '#FFC107',
                fontSize: '14px'
              }}>
                Statistics not available
              </div>
            )}
          </div>
        ) : (
          <div style={{ 
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'center',
            height: '400px',
            color: '#666',
            textAlign: 'center'
          }}>
            <div style={{
              fontSize: '48px',
              marginBottom: '20px',
              opacity: 0.3
            }}>
              👤
            </div>
            <div style={{ fontSize: '16px', marginBottom: '8px' }}>
            Select a player to view details
            </div>
            <div style={{ fontSize: '14px', opacity: 0.7 }}>
              Click on any player from the list
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default PlayersTab;

