<script setup lang="ts">
import RepositoryCloud from './Repository/RepositoryCloud.vue';
import RepositoryFork from './Repository/RepositoryFork.vue';
import RepositoryForkChart from './Repository/RepositoryForkChart.vue';
import RepositoryPieChart from './Repository/RepositoryPieChart.vue';
import RepositoryTopic from './Repository/RepositoryTopic.vue';
</script>

<template>
  <div style="text-align: center;">
    <img src="../assets/github.png" height="100" width="100" />
  </div>
  <div style="text-align:center;">
    <a href="https://github.com/" target="_blank">Github
    </a>
  </div>
  <br />
  <br />
  <RepositoryCloud />
  <RepositoryFork />
  <RepositoryTopic />
  <RepositoryForkChart />
  <RepositoryPieChart />
</template>

<style>
</style>