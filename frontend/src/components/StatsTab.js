import React, { useState, useEffect, useCallback } from 'react';
import { api } from '../services/api';
import { PieChart, Pie, Cell, BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

function StatsTab() {
  const [overallStats, setOverallStats] = useState(null);
  const [seasonStats, setSeasonStats] = useState(null);
  const [playerStats, setPlayerStats] = useState([]);
  const [loading, setLoading] = useState(true);

  const loadStats = useCallback(async () => {
    try {
      const overall = await api.getStats('All');
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
  }, []); // No dependencies needed since we're using fixed values

  useEffect(() => {
    loadStats();
  }, [loadStats]);

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  // Prepare data for charts
  const winLossData = overallStats ? [
    { name: 'Wins', value: overallStats.wins || 0, color: '#4CAF50' },
    { name: 'Losses', value: overallStats.losses || 0, color: '#F44336' }
  ] : [];

  const seasonChartData = seasonStats?.map(season => ({
    season: season.season,
    wins: season.wins || 0,
    losses: season.losses || 0,
    winPercentage: season.winPercentage || 0
  })) || [];

  const playerChartData = playerStats
    ?.sort((a, b) => (b.wins || 0) - (a.wins || 0))
    .slice(0, 10)
    .map(stat => ({
      name: `${stat.player.firstName} ${stat.player.lastName}`.substring(0, 15),
      wins: stat.wins || 0,
      losses: stat.losses || 0,
      winPercentage: stat.winPercentage || 0
    })) || [];

  const COLORS = ['#4CAF50', '#F44336', '#2196F3', '#FF9800', '#9C27B0'];

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
        
        {/* Win/Loss Pie Chart */}
        {winLossData.length > 0 && (
          <div className="chart-container">
            <h4 style={{ marginBottom: '20px', textAlign: 'center' }}>Win/Loss Distribution</h4>
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie
                  data={winLossData}
                  cx="50%"
                  cy="50%"
                  labelLine={false}
                  label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
                  outerRadius={100}
                  fill="#8884d8"
                  dataKey="value"
                >
                  {winLossData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={entry.color} />
                  ))}
                </Pie>
                <Tooltip />
                <Legend />
              </PieChart>
            </ResponsiveContainer>
          </div>
        )}
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
        
        {/* Season Comparison Bar Chart */}
        {seasonChartData.length > 0 && (
          <div className="chart-container">
            <h4 style={{ marginBottom: '20px', textAlign: 'center' }}>Season Performance Comparison</h4>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={seasonChartData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="season" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="wins" fill="#4CAF50" name="Wins" />
                <Bar dataKey="losses" fill="#F44336" name="Losses" />
              </BarChart>
            </ResponsiveContainer>
          </div>
        )}
      </div>

      <div className="stats-section">
        <h3>Individual Player Records</h3>
        
        {/* Top Players Performance Chart */}
        {playerChartData.length > 0 && (
          <div className="chart-container" style={{ marginBottom: '30px' }}>
            <h4 style={{ marginBottom: '20px', textAlign: 'center' }}>Top Players Performance</h4>
            <ResponsiveContainer width="100%" height={400}>
              <BarChart data={playerChartData} layout="vertical">
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis type="number" />
                <YAxis dataKey="name" type="category" width={120} />
                <Tooltip />
                <Legend />
                <Bar dataKey="wins" fill="#4CAF50" name="Wins" />
                <Bar dataKey="losses" fill="#F44336" name="Losses" />
              </BarChart>
            </ResponsiveContainer>
          </div>
        )}
        
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

