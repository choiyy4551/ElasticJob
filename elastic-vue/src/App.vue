<template>
  <div id="app">
    <div class="main-container">
      <header>
        <h1 class="main-title">ElasticJob</h1>
      </header>
      <nav class="sidebar">
        <ul>
          <li>
            <button @click="toggleTasksMenu" class="main-menu-item">任务管理</button>
            <ul v-show="isTasksMenuOpen" class="submenu" :class="{ show: isTasksMenuOpen }">
              <li><button @click="addTask" class="submenu-item">添加任务</button></li>
              <li><button @click="deleteTasks" class="submenu-item">删除任务</button></li>
            </ul>
          </li>
          <li>
            <button @click="toggleResultMenu" class="main-menu-item">结果管理</button>
            <ul v-show="isResultMenuOpen" class="submenu" :class="{ show: isResultMenuOpen }">
              <!-- <li><button @click="viewStatus" class="submenu-item">查看状态</button></li>
              <li><button @click="viewResultByName" class="submenu-item">查看任务结果</button></li> -->
            </ul>
          </li>
        </ul>
      </nav>
      <!-- 添加任务模态框 -->
      <div class="modal" v-if="showAddModal">
        <div class="modal-content">
          <span class="close" @click="showAddModal = false">&times;</span>
          <h3 class="modal-title">添加任务</h3>
          <div class="modal-body">
            <div class="form-group">
              <label for="name" class="form-label">任务名称</label>
              <input type="text" id="name" v-model="name" class="form-control">
            </div>
            <div class="form-group">
              <label for="param" class="form-label">任务参数</label>
              <input type="text" id="param" v-model="param" class="form-control">
            </div>
            <div class="form-group">
              <label for="scheduleType" class="form-label">任务调度类型</label>
              <select id="scheduleType" v-model="scheduleType" class="form-control">
                <option value="Once">即时任务</option>
                <option value="Time">定时任务</option>
                <option value="Daily">每日任务</option>
              </select>
            </div>
            <div class="form-group">
              <label for="scheduleParam" class="form-label">调度参数</label>
              <input type="text" id="scheduleParam" v-model="scheduleParam" class="form-control">
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-primary" @click="submitTask">提交</button>
          </div>
        </div>
      </div>
      <!-- 删除任务模态框 -->
      <div class="modal" v-if="showDeleteModal">
        <div class="modal-content">
          <span class="close" @click="showDeleteModal = false">&times;</span>
          <h3 class="modal-title">删除任务</h3>
          <div class="modal-body">
            <div class="form-group">
              <label for="name" class="form-label">任务名称</label>
              <input type="text" id="name" v-model="name" class="form-control">
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-danger" @click="deleteTask">删除</button>
          </div>
        </div>
      </div>
      <!-- 任务列表 -->
      <div class="task-list-container" v-if="showTaskList">
        <h3 class="task-list-title">任务列表</h3>
        <table class="task-list-table">
          <thead>
            <tr>
              <th>UUID</th>
              <th>任务名称</th>
              <th>任务参数</th>
              <th>调度类型</th>
              <th>调度参数</th>
              <th>最后执行时间</th>
              <th>状态</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="job in jobList" :key="job.uuid">
              <td>{{ job.uuid }}</td>
              <td>{{ job.name }}</td>
              <td>{{ job.param }}</td>
              <td>{{ job.scheduleType }}</td>
              <td>{{ job.scheduleParam }}</td>
              <td>{{ job.lastRunTime }}</td>
              <td>
                <span class="status-badge" :class="{ 'active': job.deleteStatus === 0, 'deleted': job.deleteStatus === 1 }">
                  {{ job.deleteStatus === 0 ? '启用中' : '已删除' }}
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="result-list-container" v-if="showResultList">
        <h3 class="result-list-title">结果列表</h3>
        <table class="result-list-table">
          <thead>
            <tr>
              <th>UUID</th>
              <th>任务名称</th>
              <th>结果</th>
              <th>任务状态</th>
              <th>开始时间</th>
              <th>结束时间</th>
              <th>重试次数</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="job in resultList" :key="job.uuid">
              <td>{{ job.uuid }}</td>
              <td>{{ job.name }}</td>
              <td>{{ job.result }}</td>
              <td>
                <span class="status-badge" :class="`status-${job.jobStatus}`">
                  {{ getJobStatusText(job.jobStatus) }}
                </span>
              </td>
              <td>{{ job.startTime }}</td>
              <td>{{ job.finishTime }}</td>
              <td>{{ job.failureTimes }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="modal" v-if="showViewResultModal">
        <div class="modal-content">
          <span class="close" @click="showViewResultModal = false">&times;</span>
          <h3 class="modal-title">查看任务结果</h3>
          <div class="modal-body">
            <div class="form-group">
              <label for="taskName" class="form-label">任务名称</label>
              <input type="text" id="taskName" v-model="taskName" class="form-control">
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-primary" @click="viewResultByName">提交</button>
          </div>
        </div>
      </div>

      <!-- 任务结果详情模态框 -->
      <div class="modal" v-if="showResultDetailModal">
        <div class="modal-content">
          <span class="close" @click="showResultDetailModal = false">&times;</span>
          <h3 class="modal-title">任务结果详情</h3>
          <div class="modal-body">
            <div class="form-group">
              <label class="form-label">UUID</label>
              <p>{{ resultDetail.uuid }}</p>
            </div>
            <div class="form-group">
              <label class="form-label">任务名称</label>
              <p>{{ resultDetail.name }}</p>
            </div>
            <div class="form-group">
              <label class="form-label">任务结果</label>
              <p>{{ resultDetail.result }}</p>
            </div>
            <div class="form-group">
              <label class="form-label">任务状态</label>
              <p>
                <span class="status-badge" :class="`status-${resultDetail.jobStatus}`">
                  {{ getJobStatusText(resultDetail.jobStatus) }}
                </span>
              </p>
            </div>
            <div class="form-group">
              <label class="form-label">开始时间</label>
              <p>{{ resultDetail.startTime }}</p>
            </div>
            <div class="form-group">
              <label class="form-label">结束时间</label>
              <p>{{ resultDetail.finishTime }}</p>
            </div>
            <div class="form-group">
              <label class="form-label">重试次数</label>
              <p>{{ resultDetail.failureTimes }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      showAddModal: false,
      isTasksMenuOpen: false,
      isResultMenuOpen: false,
      showDeleteModal: false,
      showTaskList: false,
      showResultList: false,
      showViewResultModal: false,
      showResultDetailModal: false,
      name: '',
      param: '',
      scheduleType: 'Once',
      scheduleParam: '',
      deleteTaskName: '',
      taskName: '',
      jobList: [],
      resultList: [],
      resultDetail: {}
    };
  },
  methods: {
    toggleTasksMenu() {
      this.isTasksMenuOpen = !this.isTasksMenuOpen;
      this.showResultList = false;
      this.isResultMenuOpen = false;
      this.showTaskList = !this.showTaskList;
      if (this.showTaskList) {
        this.viewTasks();
      } else {
        this.jobList = []; // 清空任务列表数据
      }
    },
    addTask() {
      this.showAddModal = true;
    },
    viewTasks() {
      fetch('http://127.0.0.1:8080/Job/getAllJobs')
        .then(response => response.json())
        .then(result => {
          if (result.code === 1) {
            this.jobList = result.data;
          } else {
            console.log('获取任务列表失败');
          }
        })
        .catch(error => {
          console.error('Error:', error);
        });
    },
    deleteTasks() {
      this.showDeleteModal = true;
    },
    submitTask() {
      const taskData = {
        name: this.name,
        param: this.param,
        scheduleType: this.scheduleType,
        scheduleParam: this.scheduleParam
      };
      console.log(taskData)
      fetch('http://127.0.0.1:8080/Job/addJob', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(taskData)
      })
      .then(response => response.json())
      .then(data => {
        console.log('Success:', data);
        this.showAddModal = false; // 关闭添加任务模态框
        this.resetForm();
      })
      .catch((error) => {
        console.error('Error:', error);
      });
    },
    resetForm() {
      this.name = '';
      this.param = '';
      this.scheduleType = 'Once';
      this.scheduleParam = '';
    },
    deleteTask() {
      // 发送删除请求到后端
      fetch('http://127.0.0.1:8080/Job/deleteJob', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name: this.name })
      })
      .then(response => response.json())
      .then(data => {
        console.log('Success:', data);
        this.showDeleteModal = false; // 关闭删除任务模态框
        this.name = ''; // 清空输入框
      })
      .catch((error) => {
        console.error('Error:', error);
      });
    },

    viewResults() {
      fetch('http://127.0.0.1:8080/Result/getAllResult')
        .then(response => response.json())
        .then(result => {
          if (result.code === 1) {
            this.resultList = result.data;
          } else {
            console.log('获取任务列表失败');
          }
        })
        .catch(error => {
          console.error('Error:', error);
        });
    },
    toggleResultMenu() {
      this.isResultMenuOpen = !this.isResultMenuOpen;
      this.showTaskList = false;
      this.isTasksMenuOpen = false;
      this.showResultList = !this.showResultList;
      if (this.showResultList) {
        this.viewResults();
      } else {
        this.resultList = [];
      }
    },
    getJobStatusText(status) {
      switch (status) {
        case 1:
          return 'Pending';
        case 2:
          return 'Doing';
        case 3:
          return 'Success';
        case 4:
          return 'Failed';
        case 5:
          return 'Waiting';
        case 6:
          return 'Prepared';
        default:
          return 'Unknown';
      }
    },
    viewResultByName() {
      this.showViewResultModal = false;
      fetch(`http://127.0.0.1:8080/Result/getResult?name=${this.taskName}`)
        .then(response => response.json())
        .then(result => {
          if (result.code === 1) {
            this.resultDetail = result.data;
            this.showResultDetailModal = true;
          } else {
            console.log('获取任务结果失败');
          }
        })
        .catch(error => {
          console.error('Error:', error);
        });
    }
  }
};
</script>

<style>
#app {
  display: flex;
  flex-direction: column;
  font-family: Avenir, Helvetica, Arial, sans-serif;
  color: #2c3e50;
}

header {
  background-color: #f0f0f0;
  padding: 20px;
  width: 200px;
  text-align: cen;
}
.main-container {
  display: flex;
  width: 100%;
}
.main-title {
  color: #007bff;
  font-size: 2rem;
  width: 10px;
}
.sidebar {
  background-color: #333;
  color: #fff;
  width: 200px;
  height: 2000px;
}

.sidebar ul {
  list-style-type: none;
  padding: 0;
}

.sidebar ul li {
  margin-bottom: 10px;
}

.main-menu-item {
  display: block;
  width: 100%;
  padding: 10px;
  text-align: left;
  border: none;
  background-color: transparent;
  color: #fff;
  cursor: pointer;
  font-size: 16px;
}

.main-menu-item:hover {
  background-color: #555;
}

.submenu {
  display: none;
  padding-left: 20px;
  background-color: #333;
}

.submenu.show {
  display: block;
}

.submenu-item {
  display: block;
  width: 100%;
  padding: 8px 10px;
  text-align: left;
  border: none;
  background-color: transparent;
  color: #ccc;
  cursor: pointer;
  font-size: 14px;
}

.submenu-item:hover {
  background-color: #74767c;
}

.submit-button {
  text-align: center;
}
.modal {
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0, 0, 0, 0.4);
}

.modal-content {
  background-color: #fefefe;
  padding: 20px;
  border: 1px solid #888;
  width: 400px;
}

.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}

.modal-title {
  text-align: center;
  margin-bottom: 20px;
}

.modal-body {
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-label {
  display: block;
  margin-bottom: 5px;
}

.form-control {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
}

.modal-footer {
  text-align: right;
}

.btn {
  padding: 8px 16px;
  border: none;
  cursor: pointer;
}

.btn-primary {
  background-color: #007bff;
  color: #fff;
}

.btn-danger {
  background-color: #dc3545;
  color: #fff;
}
.task-list {
  margin-left: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 8px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #f2f2f2;
}
.task-list-container {
  flex: 1;
  margin-left: 20px;
  padding: 20px;
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.task-list-title {
  margin-top: 0;
  font-size: 1.2rem;
  color: #333;
}

.task-list-table {
  width: 100%;
  border-collapse: collapse;
  border-radius: 5px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.task-list-table th,
.task-list-table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.task-list-table th {
  background-color: #f5f5f5;
  font-weight: bold;
  color: #333;
}

.task-list-table tr:last-child td {
  border-bottom: none;
}




.result-list-container {
  flex: 1;
  margin-left: 20px;
  padding: 20px;
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.result-list-title {
  margin-top: 0;
  font-size: 1.2rem;
  color: #333;
}

.result-list-table {
  width: 100%;
  border-collapse: collapse;
  border-radius: 5px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.result-list-table th,
.result-list-table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.result-list-table th {
  background-color: #f5f5f5;
  font-weight: bold;
  color: #333;
}

.result-list-table tr:last-child td {
  border-bottom: none;
}
.status-badge {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.9rem;
  font-weight: bold;
}

.status-badge.active {
  background-color: #28a745;
  color: #fff;
}

.status-badge.deleted {
  background-color: #dc3545;
  color: #fff;
}
.status-badge {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.9rem;
  font-weight: bold;
  color: #fff;
}

.status-1 {
  background-color: #007bff;
}

.status-2 {
  background-color: #ffc107;
}

.status-3 {
  background-color: #28a745;
}

.status-4 {
  background-color: #dc3545;
}

.status-5 {
  background-color: #6c757d;
}

.status-6 {
  background-color: #17a2b8;
}
</style>
