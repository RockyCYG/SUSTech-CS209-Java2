<template>
  <div id="myChart2" style="height: 1000px; margin-top: 100px">

  </div>
</template>

<script setup lang="ts">
import * as echarts from "echarts";
import { ECharts, EChartsOption } from "echarts";
import { onMounted, onUpdated, ref } from "vue";
import request from "../../utils/request";

type Tag = {
  name: string,
  scoreCount: number,
  viewCount: number
}

let myChart: ECharts;

let tagList: Tag[] = [];
let tagNameList: string[] = [];
let scoreCountList: number[] = [];
let viewCountList: number[] = [];

const colorList: string[] = [
  '#c23531',
  '#2f4554',
  '#61a0a8',
  '#d48265',
  '#91c7ae',
  '#749f83',
  '#ca8622',
  '#bda29a',
  '#6e7074',
  '#546570'
];

onMounted(() => {
  initECharts();
})

onUpdated(() => {
  initECharts();
})

const initECharts = () => {
  myChart = echarts.init(document.getElementById("myChart2")!);

  request.get("/stackoverflow/chart/tags", {
    params: {

    }
  }).then(res => {
    tagList = res.data;
    tagNameList = tagList.map(value => value.name);
    scoreCountList = tagList.map(value => value.scoreCount);
    viewCountList = tagList.map(value => value.viewCount);
    const option: EChartsOption = {
      tooltip: {
        trigger: "axis",
        axisPointer: {
          type: "shadow"
        }
      },
      title: {
        text: "Top 10 tags with highest scores in StackOverflow",
        textAlign: "left",
        left: "center",
        top: "0px",
        textStyle: {
          fontFamily: "'Open Sans Condensed', sans-serif",
          fontSize: 40,
          fontStyle: 'italic',
          fontWeight: 'bold'
        }
      },
      legend: {
        orient: "horizontal",
        left: "center",
        top: "10%"
      },
      grid: {
        left: "16%",
        right: "16%",
        bottom: "10%",
        top: "15%",
        height: '500',
        containLabel: true
      },
      xAxis: [
        {
          type: "category",
          data: tagNameList
        }
      ],
      yAxis: [
        {
          type: "value"
        }
      ],
      series: [
        {
          name: "scoreCount",
          type: "bar",
          emphasis: {
            focus: "series"
          },
          itemStyle: {
            color: (param) => {
              return colorList[param.dataIndex]
            }
          },
          data: scoreCountList
        },
        // {
        //   name: "viewCount",
        //   type: "bar",
        //   emphasis: {
        //     focus: "series"
        //   },
        //   data: viewCountList
        // }
      ]
    };

    option && myChart.setOption(option);
  })

}

</script>

<style>
#myChart2 {
  height: 4.5rem;
}
</style>