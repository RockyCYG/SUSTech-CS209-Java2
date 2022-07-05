<template>
  <h1 style="font-size: 50px;">
    Welcome to CS209 final project!
    <br />
    <br />
  </h1>
  <h2 style="font-size: 38px;">
    What are the most popular topics related to java and<br /> their developing trends
    <br />
  </h2>
  <br />
  <div style="font-size: 25px;">
    group member:<br />
    12011625 CHEN Yige<br />
    12012705 LI Jiacheng<br />
    <br />
    You can choose which you want to explore:
  </div>
  <div class="demo-image__placeholder" style="margin-top: 50px;">
    <div class="block">
      <span class="demonstration"></span>
      <el-image :src="stackoverflow" />
    </div>
    <div class="block">
      <span class="demonstration"></span>
      <el-image :src="github">
        <template #placeholder>
          <div class="image-slot">Loading<span class="dot">...</span></div>
        </template>
      </el-image>
    </div>
  </div>
  <span style="font-size: 20px; margin-left: 240px;">StackOverflow</span>
  <span style="font-size: 20px; margin-left: 530px;">Github</span>
  <br />
  <router-link to="/stackoverflow/chart" style="font-size: 20px; margin-left: 200px;">Chart</router-link>
  <router-link to="/stackoverflow/table" style="font-size: 20px; margin-left: 110px;">Table</router-link>
  <router-link to="/github/chart" style="font-size: 20px; margin-left: 450px;">Chart</router-link>
  <router-link to="/github/chart" style="font-size: 20px; margin-left: 50px;">Table</router-link>
</template>

<script lang="ts" setup>
import stackoverflow from '../assets/stackoverflow.png';
import github from '../assets/github.png';
</script>

  <style scoped>
.demo-image__placeholder .block {
  padding: 30px 0;
  text-align: center;
  border-right: solid 1px var(--el-border-color);
  display: inline-block;
  width: 49%;
  box-sizing: border-box;
  vertical-align: top;
}

.demo-image__placeholder .demonstration {
  display: block;
  color: var(--el-text-color-secondary);
  font-size: 14px;
  margin-bottom: 20px;
}

.demo-image__placeholder .el-image {
  padding: 0 5px;
  max-width: 300px;
  max-height: 300px;
}

.demo-image__placeholder.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: var(--el-fill-color-light);
  color: var(--el-text-color-secondary);
  font-size: 14px;
}

.demo-image__placeholder .dot {
  animation: dot 2s infinite steps(3, start);
  overflow: hidden;
}
</style>