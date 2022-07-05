<template>
    <div class="common-layout">
        <el-container>
            <el-aside>
                <AppMenu />
            </el-aside>
            <el-container>
                <el-header>
                    <div style="margin-top: 20px">
                        <!-- <el-icon :collapse="isCollapse" @click="setCollapse">
                            <Expand />
                        </el-icon> -->
                        <el-breadcrumb :separator-icon="ArrowRight">
                            <el-breadcrumb-item :to="{ path: item.path }" v-for="(item, index) in $route.matched"
                                :key="index">{{ item.name }}
                            </el-breadcrumb-item>
                        </el-breadcrumb>
                    </div>
                </el-header>
                <el-main>
                    <router-view />
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script lang="ts" setup>
import AppMenu from './components/AppMenu.vue';
import { ArrowRight, Expand } from '@element-plus/icons-vue'
import { ref } from 'vue';

let isCollapse = ref(false);

const setCollapse = () => {
    isCollapse.value = !isCollapse.value;
}
</script>

<style scoped>
.el-header,
.el-footer {
    background-color: #B3C0D1;
    color: #333;
}

.el-aside {
    width: auto;
    background-color: #304156;
    color: #333;
}

.el-main {
    background-color: #ffffff;
    color: #333;
}

.el-container {
    height: 100vh;
}
</style>
