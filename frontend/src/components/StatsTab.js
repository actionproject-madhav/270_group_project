import React, { useState, useEffect } from 'react';
import { api } from '../services/api';

function StatsTab() {
  const [overallStats, setOverallStats] = useState(null);
  const [seasonStats, setSeasonStats] = useState(null);
  const [playerStats, setPlayerStats] = useState([]);
  const [selectedSeason, setSelectedSeason] = useState('All');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadStats();
  }, [selectedSeason]);

  const loadStats = async () => {
    try {
      const overall = await api.getStats(selectedSeason);
      setOverallStats(overall);
      
      const allPlayers = await api.getPlayers();
      const statsPromises = allPlayers.map(async (player) => {
        try {
          const stats = await api.getPlayerStats(player.id);
          return { player, ...stats };
        } catch (error) {
          return { player, wins: 0, losses: 0, winPercentage: 0 };
        }
      });
      const stats = await Promise.all(statsPromises);
      setPlayerStats(stats);
      
      const seasons = ['2024', '2023', '2022'];
      const seasonDataPromises = seasons.map(async (season) => {
        const data = await api.getStats(season);
        return { season, ...data };
      });
      const seasonData = await Promise.all(seasonDataPromises);
      setSeasonStats(seasonData);
      
      setLoading(false);
    } catch (error) {
      console.error('Error loading stats:', error);
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  return (
    <div className="stats-container">
      <div className="stats-section">
        <h3>Overall Team Record</h3>
        <div className="overall-stats">
          <div className="stat-box">
            <div className="stat-value">{overallStats?.wins || 0}</div>
            <div className="stat-label">Total Wins</div>
          </div>
          <div className="stat-box">
            <div className="stat-value">{overallStats?.losses || 0}</div>
            <div className="stat-label">Total Losses</div>
          </div>
          <div className="stat-box">
            <div className="stat-value">{overallStats?.winPercentage?.toFixed(1) || 0}%</div>
            <div className="stat-label">Win Percentage</div>
          </div>
        </div>
      </div>

      <div className="stats-section">
        <h3>Season Breakdown</h3>
        <div className="season-stats">
          {seasonStats?.map((season) => (
            <div key={season.season} className="stat-box">
              <div className="stat-value">{season.season}</div>
              <div className="stat-label">{season.wins}W - {season.losses}L</div>
              <div className="stat-label" style={{ fontSize: '11px', marginTop: '5px' }}>
                {season.winPercentage?.toFixed(1)}% Win Rate
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className="stats-section">
        <h3>Individual Player Records</h3>
        <table className="player-stats-table">
          <thead>
            <tr>
              <th>Player</th>
              <th>Class</th>
              <th>Wins</th>
              <th>Losses</th>
              <th>Win %</th>
            </tr>
          </thead>
          <tbody>
            {playerStats?.map((stat, index) => (
              <tr key={index}>
                <td>{stat.player.firstName} {stat.player.lastName}</td>
                <td>{stat.player.classYear}</td>
                <td>{stat.wins}</td>
                <td>{stat.losses}</td>
                <td>{stat.winPercentage?.toFixed(1) || 0}%</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default StatsTab;

