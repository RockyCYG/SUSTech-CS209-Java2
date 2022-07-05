<template>
  <div id="fork">

  </div>
</template>

<script setup lang="ts">
import * as echarts from 'echarts'
import request from '../../utils/request';
import { onMounted } from "vue";

type RepositoryFork = {
  title: string,
  forkCount: number,
  time: string
}

onMounted(() => {
  type EChartsOption = echarts.EChartsOption;

  var chartDom = document.getElementById('fork')!;
  var myChart = echarts.init(chartDom);

  var option: EChartsOption;
  const updateFrequency = 500;
  const dimension = 0;

  request.get("/github/chart/fork", {
    params: {}
  })
    .then(function (res) {
      const data: RepositoryFork[] = res.data;
      // console.log(data);

      const years: string[] = [];
      for (let i = 0; i < data.length; ++i) {
        if (years.length === 0 || !years.includes(data[i].time)) {
          years.push(data[i].time);
        }
      }
      years.sort();

      let startIndex = 0;
      let startYear = years[startIndex];

      const colors: string[] = ['#ff6600', '#f00', '#ffde00', '#002a8f', '#003580', '#ed2939', '#008000', '#f93', '#bc002d', '#5470c6']
      const forkCountColors: string[] = [];

      const repos: string[] = Array.from(new Set(data.map(d => d.title)));
      let reposToIndex = new Map();
      let forkValue: number[] = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0];

      repos.forEach((value, index, array) => {
        forkCountColors[index] = colors[index];
        reposToIndex.set(value, index);
        forkValue[index] = 0;
      })

      option = {
        grid: {
          top: 80,
          bottom: 30,
          left: 200,
          right: 80,
          height: 500
        },
        title: {
          text: "Github Repos Total Forks in Time",
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
        xAxis: {
          max: 'dataMax',
          axisLabel: {
            formatter: function (n: number) {
              return Math.round(n) + '';
            }
          }
        },
        dataset: {
          source: data.filter(d => d.time === startYear)
        },
        yAxis: {
          data: repos,
          type: 'category',
          inverse: true,
          max: 10,
          axisLabel: {
            show: true,
            fontSize: 14,
            rich: {
              flag: {
                fontSize: 25,
                padding: 5
              }
            }
          },
          animationDuration: 300,
          animationDurationUpdate: 300
        },
        series: [
          {
            realtimeSort: true,
            seriesLayoutBy: 'column',
            type: 'bar',
            itemStyle: {
              color: function (param) {
                return forkCountColors[param.dataIndex] || '#5470c6';
              }
            },
            encode: {
              x: dimension,
              y: 3
            },
            label: {
              show: true,
              precision: 1,
              position: 'right',
              valueAnimation: true,
              fontFamily: 'monospace'
            }
          }
        ],
        // Disable init animation.
        animationDuration: 0,
        animationDurationUpdate: updateFrequency,
        animationEasing: 'linear',
        animationEasingUpdate: 'linear',
        graphic: {
          elements: [
            {
              type: 'text',
              right: 160,
              bottom: 60,
              style: {
                text: startYear,
                font: 'bolder 80px monospace',
                fill: 'rgba(100, 100, 100, 0.25)'
              },
              z: 100
            }
          ]
        }
      };

      myChart.setOption<echarts.EChartsOption>(option);

      for (let i = startIndex; i < years.length - 1; ++i) {
        (function (i) {
          setTimeout(function () {
            updateYear(years[i + 1]);
          }, (i - startIndex) * updateFrequency);
        })(i);
      }

      function updateYear(year: string) {
        let source = data.filter(function (d: RepositoryFork) {
          return d.time === year;
        });

        for (let i = 0; i < source.length; i++) {
          const fork = source[i];
          const index = reposToIndex.get(fork.title);
          forkValue[index] = fork.forkCount;
        }

        (option as any).series[0].data = forkValue;
        (option as any).graphic.elements[0].style.text = year;
        (option as any).graphic.elements[0].style.x = '550';
        (option as any).graphic.elements[0].style.y = '5';

        myChart.setOption<echarts.EChartsOption>(option);
      }
    });

})
</script>

<style>
#fork {
  height: 800px;
  padding-left: 50px;
}
</style>