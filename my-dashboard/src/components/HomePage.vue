<template>
  <div>
    <v-container>
      <v-row>
        <!-- Card para Total de Clientes -->
        <v-col cols="12" sm="6" md="4">
          <v-card class="mx-auto" max-width="400">
            <v-card-title>Total Tasks</v-card-title>
            <v-card-text class="d-flex justify-center align-center">
              <span class="display-2">{{ totalTasks }}</span>
            </v-card-text>
          </v-card>
        </v-col>

        <!-- Card para Total de Conectores -->
        <v-col cols="12" sm="6" md="4">
          <v-card class="mx-auto" max-width="400">
            <v-card-title>Total Conectores</v-card-title>
            <v-card-text class="d-flex justify-center align-center">
              <span class="display-2">{{ totalConnectors }}</span>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tasks: [],
      totalTasks: 0,
      totalConnectors: 0
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      this.$api.get('/scheduled-tasks')
        .then(response => {
          this.tasks = response.data;
          this.totalTasks = this.tasks.length;
          this.totalConnectors = this.clientes.reduce((sum, item) => {
            return sum + item.active;
          }, 0);
        })
        .catch(error => {
          console.error('Error fetching data:', error);
        });
    },
    viewDetails(clientName) {
      this.$router.push({ name: 'ConnectorList', params: { clientName } });
    },
    viewVolumetry(clientName) {
      this.$router.push({ name: 'VolumetryDetails', params: { clientName } });
    }
  }
};
</script>
