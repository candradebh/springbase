import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    user: JSON.parse(localStorage.getItem('user')) || null,
  },
  mutations: {
    SET_USER(state, user) {
      state.user = user;
      if (user) {
        localStorage.setItem('user', JSON.stringify(user));
      } else {
        localStorage.removeItem('user');
      }
    }
  },
  actions: {
    fetchUserProfile({ commit }) {
      return new Promise((resolve, reject) => {
        Vue.prototype.$api.get('/auth/profile')
          .then(response => {
            const user = response.data;
            commit('SET_USER', user);
            resolve(user);
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    updateUserProfile({ commit }, profileData) {
      return new Promise((resolve, reject) => {
        Vue.prototype.$api.put('/auth/profile', profileData)
          .then(response => {
            const updatedUser = response.data;
            commit('SET_USER', updatedUser);
            resolve(updatedUser);
          })
          .catch(error => {
            reject(error);
          });
      });
    }
  },
  getters: {
    user: state => state.user,
    isAuthenticated: state => !!state.user,
  }
});
