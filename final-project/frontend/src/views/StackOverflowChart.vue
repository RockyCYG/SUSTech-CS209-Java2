<script setup lang="ts">
import QuestionChart from './Question/QuestionChart.vue';
import QuestionTagCloud from './Question/QuestionTagCloud.vue';
import QuestionViewCount from './Question/QuestionViewCount.vue';

</script>

<template>
  <div style="text-align: center;">
    <img src="../assets/stackoverflow.png" height="100" width="100" />
  </div>
  <div style="text-align:center;">
    <a href="https://stackoverflow.com/" target="_blank">StackOverflow
    </a>
  </div>
  <br />
  <br />
  <QuestionTagCloud />
  <QuestionChart />
  <QuestionViewCount />
</template>

<style>
</style>