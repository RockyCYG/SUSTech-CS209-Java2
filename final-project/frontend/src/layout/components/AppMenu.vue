<template>
    <el-menu active-text-color="#ffd04b" background-color="#304156" class="el-menu-vertical-demo" default-active="2"
        text-color="#fff" router>
        <el-menu-item index="/">
            <el-icon>
                <HomeFilled />
            </el-icon>
            <span>Home</span>
        </el-menu-item>

        <el-sub-menu index="">
            <template #title>
                <el-icon>
                    <View />
                </el-icon>
                <span>StackOverflow</span>
            </template>
            <el-menu-item index="/stackoverflow/chart">
                <el-icon>
                    <TrendCharts />
                </el-icon>
                <span>Chart</span>
            </el-menu-item>
            <el-menu-item index="/stackoverflow/table">
                <el-icon>
                    <Grid />
                </el-icon>
                <span>Table</span>
            </el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="">
            <template #title>
                <el-icon>
                    <View />
                </el-icon>
                <span>Github</span>
            </template>
            <el-menu-item index="/github/chart">
                <el-icon>
                    <TrendCharts />
                </el-icon>
                <span>Chart</span>
            </el-menu-item>
            <el-menu-item index="/github/table">
                <el-icon>
                    <Grid />
                </el-icon>
                <span>Table</span>
            </el-menu-item>
        </el-sub-menu>
    </el-menu>
</template>

<script setup lang="ts">
import { HomeFilled, View, TrendCharts, Grid } from '@element-plus/icons-vue';
</script>

<style scoped>
.el-menu {
    border-right: none;
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 200px;
    min-height: 400px;
}
</style>