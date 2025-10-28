const API_BASE_URL = 'http://localhost:8080/api';

export const api = {
  async getPlayers() {
    const response = await fetch(`${API_BASE_URL}/players`);
    return response.json();
  },

  async getMatches(filters = {}) {
    const params = new URLSearchParams();
    if (filters.season && filters.season !== 'All') {
      params.append('season', filters.season);
    }
    if (filters.type && filters.type !== 'All') {
      params.append('type', filters.type);
    }
    if (filters.opponent) {
      params.append('opponent', filters.opponent);
    }
    
    const url = params.toString() 
      ? `${API_BASE_URL}/matches?${params.toString()}`
      : `${API_BASE_URL}/matches`;
    
    const response = await fetch(url);
    return response.json();
  },

  async getStats(season = null) {
    const url = season && season !== 'All'
      ? `${API_BASE_URL}/stats?season=${season}`
      : `${API_BASE_URL}/stats`;
    
    const response = await fetch(url);
    return response.json();
  },

  async getPlayerStats(playerId) {
    const response = await fetch(`${API_BASE_URL}/player/${playerId}`);
    return response.json();
  }
};

