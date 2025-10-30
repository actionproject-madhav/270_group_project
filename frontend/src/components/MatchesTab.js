import React, { useState, useEffect, useCallback } from 'react';
import { api } from '../services/api';

function MatchesTab() {
  const [matches, setMatches] = useState([]);
  const [selectedMatch, setSelectedMatch] = useState(null);
  const [filters, setFilters] = useState({
    season: 'All',
    type: 'All',
    opponent: ''
  });
  const [loading, setLoading] = useState(true);

  const loadMatches = useCallback(async () => {
    try {
      const data = await api.getMatches(filters);
      setMatches(data);
      setLoading(false);
    } catch (error) {
      console.error('Error loading matches:', error);
      setLoading(false);
    }
  }, [filters]);

  useEffect(() => {
    loadMatches();
  }, [loadMatches]);

  const handleFilterChange = (filterName, value) => {
    setFilters({ ...filters, [filterName]: value });
  };

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  return (
    <div className="matches-container">
      <div className="matches-table-container">
        <div className="matches-table-header">
          <h3>Matches ({matches.length})</h3>
          <div className="filters">
            <div className="filter-group">
              <label>Season:</label>
              <select
                value={filters.season}
                onChange={(e) => handleFilterChange('season', e.target.value)}
              >
                <option value="All">All</option>
                <option value="2024">2024</option>
                <option value="2023">2023</option>
                <option value="2022">2022</option>
              </select>
            </div>
            <div className="filter-group">
              <label>Type:</label>
              <select
                value={filters.type}
                onChange={(e) => handleFilterChange('type', e.target.value)}
              >
                <option value="All">All</option>
                <option value="Singles">Singles</option>
                <option value="Doubles">Doubles</option>
              </select>
            </div>
            <div className="filter-group">
              <label>Opponent:</label>
              <input
                type="text"
                placeholder="Search..."
                value={filters.opponent}
                onChange={(e) => handleFilterChange('opponent', e.target.value)}
              />
            </div>
          </div>
        </div>
        <table className="matches-table">
          <thead>
            <tr>
              <th>Date</th>
              <th>Type</th>
              <th>Opponent</th>
              <th>Score</th>
              <th>Result</th>
            </tr>
          </thead>
          <tbody>
            {matches.map(match => (
              <tr
                key={match.id}
                onClick={() => setSelectedMatch(match)}
                style={{ cursor: 'pointer' }}
              >
                <td>{match.date}</td>
                <td><span className="match-type">{match.type}</span></td>
                <td>{match.opponent}</td>
                <td>{match.score}</td>
                <td>
                  <span className={`win-indicator ${match.won ? 'won' : 'lost'}`}></span>
                  {match.won ? 'Win' : 'Loss'}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="match-details">
        {selectedMatch ? (
          <>
            <h3>Match Details</h3>
            <div className="detail-row">
              <div className="detail-label">Date</div>
              <div className="detail-value">{selectedMatch.date}</div>
            </div>
            <div className="detail-row">
              <div className="detail-label">Season</div>
              <div className="detail-value">{selectedMatch.season}</div>
            </div>
            <div className="detail-row">
              <div className="detail-label">Type</div>
              <div className="detail-value">{selectedMatch.type}</div>
            </div>
            <div className="detail-row">
              <div className="detail-label">Opponent</div>
              <div className="detail-value">{selectedMatch.opponent}</div>
            </div>
            <div className="detail-row">
              <div className="detail-label">Score</div>
              <div className="detail-value">{selectedMatch.score}</div>
            </div>
            <div className="detail-row">
              <div className="detail-label">Result</div>
              <div className="detail-value">{selectedMatch.won ? 'Win' : 'Loss'}</div>
            </div>
            <div className="detail-row">
              <div className="detail-label">Rollins Players</div>
              <div className="detail-value">{selectedMatch.rollinsPlayer}</div>
            </div>
            <div className="detail-row">
              <div className="detail-label">Opponent Players</div>
              <div className="detail-value">{selectedMatch.opponentPlayer}</div>
            </div>
          </>
        ) : (
          <div style={{ color: '#666', textAlign: 'center', marginTop: '50%' }}>
            Select a match to view details
          </div>
        )}
      </div>
    </div>
  );
}

export default MatchesTab;

