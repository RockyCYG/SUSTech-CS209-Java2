<template>
  <h1 style="text-align: center; font-size: 35px;">You can search all the information here</h1>
  <div style="padding-top: 20px;">
    <div>
      <el-input :prefix-icon="Search" style="width: 200px; padding-left: 30px" v-model="id" placeholder="id"
        :disabled="listLoading" clearable />
      <el-input :prefix-icon="Search" style="width: 200px; padding-left: 5px" v-model="title" placeholder="title"
        :disabled="listLoading" clearable />
      <el-input :prefix-icon="Search" style="width: 200px; padding-left: 5px" v-model="forks" placeholder=">=forks"
        :disabled="listLoading" clearable />
      <el-input :prefix-icon="Search" style="width: 200px; padding-left: 5px" v-model="watchers" placeholder=">=stars"
        :disabled="listLoading" clearable />
      <!-- <el-input :prefix-icon="Search" style="width: 200px; padding-left: 5px" v-model="createdTime"
        placeholder="createdTime" :disabled="listLoading" clearable /> -->
      <el-date-picker v-model="createdTime" type="month" placeholder="createTime"
        style="width: 200px; padding-left: 5px" :disabled="listLoading" clearable />
      <!-- <el-input :prefix-icon="Search" style="width: 200px; padding-left: 5px" v-model="tags" placeholder="tags"
:disabled="listLoading" clearable /> -->

      <el-button type="primary" style="width: 100px; margin-left: 10px;" :icon="Search" @click="search"
        :disabled="listLoading">
        Search
      </el-button>
      <el-button type="primary" style="width: 100px; margin-left: 10px;" :icon="Download" @click="exportData"
        :disabled="listLoading">
        Export
      </el-button>
    </div>
    <span style="padding-left: 15px;">
      <el-tag v-for="(tag, index) in dynamicTags" :key="tag" class="mx-1" closable :disable-transitions="false"
        @close="handleClose(tag)" style="margin-left: 15px; margin-top: 10px;"
        :type="tagTypes[index % tagTypes.length]">
        {{ tag }}
      </el-tag>
    </span>
    <span style="margin-top: 20px;">
      <el-input v-if="inputVisible" ref="InputRef" v-model="inputValue" class="ml-1 w-20" size="small"
        style="width: 100px; margin-left: 15px;" @keyup.enter="handleInputConfirm" @blur="handleInputConfirm" />
      <el-button v-else class="button-new-tag ml-1" size="small" @click="showInput" style="margin-left: 15px;">
        + New Tag
      </el-button>
    </span>
    <div>
      <el-table :data="tableData" style="width: 100%; margin-top: 10px;" v-loading="listLoading"
        :row-class-name="tableRowClassName" @sort-change="sortChange">
        <el-table-column prop="id" label="Id" sortable="custom" width="180" align="center" header-align="center" />
        <el-table-column prop="title" label="Title" width="300" align="center" header-align="center" />
        <el-table-column prop="forks" label="forks" sortable="custom" width="180" align="center"
          header-align="center" />
        <el-table-column prop="watchers" label="stars" sortable="custom" width="180" align="center"
          header-align="center" />
        <el-table-column prop="createdTime" label="createdTime" sortable="custom" width="180" align="center"
          header-align="center" />
        <el-table-column prop="tags" label="Tags" width="180" align="center" header-align="center">
          <template #default="{ row }">
            <div v-for="(item, index) in row.tags" :key="index" style="margin-top: 3%;">
              <el-tag :type="tagTypes[index % tagTypes.length]" effect="dark" round>
                {{ item }}
              </el-tag>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="demo-pagination-block">
      <div class="demonstration"></div>
      <el-pagination v-model:currentPage="pageNum" v-model:page-size="pageSize"
        :page-sizes="[5, 10, 50, 100, 200, 1000, 10000]" :small="small" :disabled="listLoading" :background="background"
        layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange"
        style="padding-left: 3%; margin-bottom: 300px;" @current-change="handleCurrentChange" />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, ref, nextTick } from "vue";
import { Search, Download } from '@element-plus/icons-vue';
import request from "../../utils/request";
import type { ElInput } from 'element-plus';

type Repository = {
  id: number,
  title: string,
  forks: number,
  watchers: number,
  createdTime: string,
  tags: string[]
}

type QueryList = {
  prop: string,
  order: number // asc is 0, desc is 1
}

const column = ref('id');
const sort = ref(0);

const pageNum = ref(1);
const pageSize = ref(10);
const small = ref(false);
const background = ref(false);
const total = ref(0);

const listLoading = ref(true);

const id = ref<number | null>(null);
const title = ref<string | null>(null);
const forks = ref<number | null>(null);
const watchers = ref<number | null>(null);
const createdTime = ref<number | null>(null);
const tags = ref<string[] | null>(null);

const tagTypes: string[] = ['', 'success', 'info', 'warning', 'danger'];

const handleSizeChange = (pageSize: number) => {
  load();
}
const handleCurrentChange = (pageNum: number) => {
  load();
}

const tableData = ref<Repository[]>([]);

onMounted(() => {
  load();
})

const load = () => {
  listLoading.value = true;
  request.get("/github/table/pages", {
    params: {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      column: column.value,
      sort: sort.value,
      id: id.value,
      title: title.value,
      watchers: watchers.value,
      forks: forks.value,
      createdTime: createdTime.value,
      dynamicTags: dynamicTags.value + ''
    }
  }).then(res => {
    tableData.value = res.data.dataList;
    total.value = res.data.totalCount;
    console.log(tableData);
  }).finally(() => {
    listLoading.value = false;
  })
}

const exportData = () => {
  listLoading.value = true;
  request.get("/stackoverflow/table/export", {
    params: {}
  }).then(res => {

  }).finally(() => {
    listLoading.value = false;
  })
}

const sortChange = (data: any) => {
  // console.log(data);
  const { prop, order }: { prop: string, order: string } = data;
  column.value = prop;
  sort.value = order === 'descending' ? 1 : 0;
  load();
}

const search = () => {
  load();
}

const inputValue = ref('')
const dynamicTags = ref([])
const inputVisible = ref(false)
const InputRef = ref<InstanceType<typeof ElInput>>()

const handleClose = (tag: string) => {
  dynamicTags.value.splice(dynamicTags.value.indexOf(tag), 1);
  load();
}

const showInput = () => {
  inputVisible.value = true;
  nextTick(() => {
    InputRef.value!.input!.focus();
  })
}

const handleInputConfirm = () => {
  if (inputValue.value) {
    dynamicTags.value.push(inputValue.value);
  }
  inputVisible.value = false;
  inputValue.value = '';
  load();
}

const tableRowClassName = ({
  row,
  rowIndex,
}: {
  row: Repository
  rowIndex: number
}) => {
  if (rowIndex % 4 === 1) {
    return 'success-row'
  } else if (rowIndex % 4 === 3) {
    return 'warning-row'
  }
  return ''
}

</script>

<style>
.demo-pagination-block {
  margin-top: 10px;
}

.demo-pagination-block .demonstration {
  margin-bottom: 16px;
}

.el-table .warning-row {
  --el-table-tr-bg-color: var(--el-color-warning-light-9);
}

.el-table .success-row {
  --el-table-tr-bg-color: var(--el-color-success-light-9);
}
</style>
