<template>
  <el-container class="layout-container-demo" style="height: 100vh">

    <el-container>
      <el-header class="text-left el-header-custom w-full">
        <div class="toolbar flex items-center w-full">
          <el-text>SpringBoot-Generate</el-text>


          <svg @click="goToGithub" class="max-w-7 max-h-7 ms-auto svg-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="64" height="64">
            <path d="M511.6 76.3C264.3 76.2 64 276.4 64 523.5 64 718.9 189.3 885 363.8 946c23.5 5.9 19.9-10.8 19.9-22.2v-77.5c-135.7 15.9-141.2-73.9-150.3-88.9C215 726 171.5 718 184.5 703c30.9-15.9 62.4 4 98.9 57.9 26.4 39.1 77.9 32.5 104 26 5.7-23.5 17.9-44.5 34.7-60.8-140.6-25.2-199.2-111-199.2-213 0-49.5 16.3-95 48.3-131.7-20.4-60.5 1.9-112.3 4.9-120 58.1-5.2 118.5 41.6 123.2 45.3 33-8.9 70.7-13.6 112.9-13.6 42.4 0 80.2 4.9 113.5 13.9 11.3-8.6 67.3-48.8 121.3-43.9 2.9 7.7 24.7 58.3 5.5 118 32.4 36.8 48.9 82.7 48.9 132.3 0 102.2-59 188.1-200 212.9 23.5 23.2 38.1 55.4 38.1 91v112.5c0.8 9 0 17.9 15 17.9 177.1-59.7 304.6-227 304.6-424.1 0-247.2-200.4-447.3-447.5-447.3z" fill="#737478"></path>
          </svg>
        </div>



      </el-header>

      <el-main>
        <div>
          <el-form :model="formLabelAlign" ref="queryForm" :inline="true"
                   class="px-5 pt-5">
            <el-form-item label="包名" prop="packageBase">
              <el-input
                  v-model="formLabelAlign.packageBase"
                  placeholder="例如: com.dullfan.hahah"
                  clearable
                  @keyup.enter.native="handleQuery"
              />
            </el-form-item>

            <el-form-item label="忽略的字段" prop="fieldIgnoreList">
              <el-input
                  v-model="formLabelAlign.fieldIgnoreList"
                  placeholder="例如: id,status"
                  clearable
                  @keyup.enter.native="handleQuery"
              />
            </el-form-item>

            <el-form-item label="作者" prop="author">
              <el-input
                  v-model="formLabelAlign.author"
                  placeholder="例如: DullFan"
                  clearable
                  @keyup.enter.native="handleQuery"
              />
            </el-form-item>

            <el-form-item label="SpringBoot版本" prop="springBootGenerateVersion">
              <el-select v-model="formLabelAlign.springBootGenerateVersion" placeholder="Select" style="width: 80px">
                <el-option
                    v-for="item in springBootVersionOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-form>
        </div>

        <el-row class="px-5">
          <el-button
              type="primary"
              :disabled="generateButtonFlag"
              plain
              @click="databaseCreateClick"
              :icon="Download">生成
          </el-button>
          <el-button
              type="primary"
              plain
              :icon="Plus"
              @click="sqlCreateClick">SQL生成
          </el-button>

          <el-button
              type="success"
              plain
              :icon="Bicycle"
              @click="configurationDatabase">配置数据库
          </el-button>
        </el-row>

        <div class="px-5 pt-5">
          <el-table :data="tableData"
                    :header-cell-style="{background:'#f8f8f9'}"
                    @selection-change="handleSelectionChange">
            <el-table-column align="center" type="selection" width="55"/>
            <el-table-column align="center" type="index" label="序号" min-width="100"/>
            <el-table-column align="center" prop="tableName" label="表名" min-width="180"/>
            <el-table-column align="center" prop="beanName" label="实体" min-width="180"/>
            <el-table-column align="center" prop="comment" label="表描述" min-width="180"/>
          </el-table>
        </div>
      </el-main>
    </el-container>
  </el-container>


  <el-dialog v-model="dialogFormVisible" title="配置数据库"
             width="500">
    <el-form :model="formLabelAlign" ref="queryForm"
             label-position="right"
             label-width="auto"
             class="px-5 pt-5">
      <el-form-item label="地址" prop="sqlIp">
        <el-input
            v-model="formLabelAlign.sqlIp"
            placeholder="例如: 127.0.0.1"
            clearable
            show-password
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="端口" prop="ipPort">
        <el-input
            v-model="formLabelAlign.ipPort"
            placeholder="例如: 3306"
            clearable
            show-password
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="用户名" prop="sqlUsername">
        <el-input
            v-model="formLabelAlign.sqlUsername"
            placeholder="例如: DullFan"
            clearable
            show-password
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="密码" prop="sqlPassword">
        <el-input
            v-model="formLabelAlign.sqlPassword"
            placeholder="例如: 123456"
            clearable
            show-password
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="数据库" prop="sqlPassword">
        <el-input
            v-model="formLabelAlign.sqlName"
            placeholder="例如: code_test"
            clearable
            show-password
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="databaseConfigClick">
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>


  <el-dialog v-model="dialogSQLVisible" title="SQL生成"
             width="500">

    <el-form-item prop="sqlPassword">
      <el-input
          :rows="10"
          type="textarea"
          v-model="formLabelAlign.sqlStatement"
          placeholder="只限Create"
          clearable
      />
    </el-form-item>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogSQLVisible = false">取消</el-button>
        <el-button type="primary" @click="sqlCreateClickSure">
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import {getCurrentInstance, reactive, ref} from 'vue'
import {
  Download,
  Plus,
  Bicycle,
} from "@element-plus/icons-vue";
import axios from "axios";
import {ElNotification} from "element-plus";

const dialogSQLVisible = ref(false)
const dialogFormVisible = ref(false)

const formLabelAlign = ref({
  packageBase: '',
  fieldIgnoreList: '',
  author: '',
  springBootGenerateVersion: 3,
  sqlIp: '',
  ipPort: '',
  sqlName: '',
  sqlUsername: '',
  sqlPassword: '',
  databaseName: '',
  sqlStatement: '',
})

function goToGithub() {
  window.open("https://github.com/DullFan/springboot-generate", '_blank')
}

const generateButtonFlag = ref(true)


const springBootVersionOptions = [
  {
    value: '2',
    label: '2',
  },
  {
    value: '3',
    label: '3',
  }
]

let tableData = ref([])

function handleQuery() {

}

function sqlCreateClick() {
  dialogSQLVisible.value = true
}

function configurationDatabase() {
  dialogFormVisible.value = true
}

const api = getCurrentInstance().appContext.config.globalProperties.$api;

function databaseConfigClick() {
  if (formLabelAlign.value.sqlName.trim() === ''
      || formLabelAlign.value.sqlIp.trim() === ''
      || formLabelAlign.value.sqlUsername.trim() === ''
      || formLabelAlign.value.sqlPassword.trim() === ''
      || formLabelAlign.value.ipPort.trim() === ''
  ) {
    ElNotification({
      title: 'Error',
      message: "请填写完整数据库数据",
      type: 'error',
    })
  } else {
    axios({
      method: "post",
      url: `${api}/findAllSQLStructure`,
      data: formLabelAlign.value
    }).then(function (resp) {
      if (resp.data.code == 500) {
        ElNotification({
          title: 'Error',
          message: resp.data.msg,
          type: 'error',
        })
      } else {
        tableData.value = resp.data.data
      }
    })
    dialogFormVisible.value = false
  }
}

function sqlCreateClickSure() {
  formLabelAlign.value.sqlStatement = formLabelAlign.value.sqlStatement.replace(/(\r\n|\n|\r)/gm, "");
  if (formLabelAlign.value.packageBase.trim() === '') {
    formLabelAlign.value.packageBase = "com.dullfan"
  }
  if (formLabelAlign.value.author.trim() === '') {
    formLabelAlign.value.author = "DullFan"
  }
  const sql = formLabelAlign.value.sqlStatement.trim().toLowerCase();

  if (!sql.includes('create')) {
    ElNotification({
      title: 'Error',
      message: "SQL不包含Create",
      type: 'error',
    })
    return
  }

  axios({
    method: "post",
    url: `${api}/findAllSQLResource`,
    data: formLabelAlign.value,
    responseType: 'blob'
  }).then(function (resp) {
    let filename = "code.zip";
    const url = window.URL.createObjectURL(new Blob([resp.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', filename);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  }).catch(error => {
    console.error('下载文件失败:', error);
  });
  dialogFormVisible.value = false
}

function databaseCreateClick() {
  if (formLabelAlign.value.packageBase.trim() === '') {
    formLabelAlign.value.packageBase = "com.dullfan"
  }
  if (formLabelAlign.value.author.trim() === '') {
    formLabelAlign.value.author = "DullFan"
  }
  formLabelAlign.value.sqlStatement = ''
  axios({
    method: "post",
    url: `${api}/findSQLResource`,
    data: formLabelAlign.value,
    responseType: 'blob'
  }).then(function (resp) {
    let filename = "code.zip";
    const url = window.URL.createObjectURL(new Blob([resp.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', filename);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  }).catch(error => {
    console.error('下载文件失败:', error);
  });
  dialogFormVisible.value = false
}


function handleSelectionChange(selection) {
  generateButtonFlag.value = selection.length == 0
  formLabelAlign.value.databaseName = selection.map(item => item.tableName).join(',');
}


</script>

<style scoped>
.el-header-custom {
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.layout-container-demo .el-header {
  position: relative;
}

.layout-container-demo .el-main {
  padding: 0;
}

.layout-container-demo .toolbar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  right: 20px;
}

.layout-container-demo .auto {
  display: inline-flex;
  align-items: center;
  flex-basis: auto;
}

.layout-container-demo .github {
  display: inline-flex;
  align-items: center;
  justify-content: end;
  height: 100%;
  right: 20px;
}

.github-wrapper {
  margin-left: auto; /* This will push the GitHub link to the right */
}

.svg-icon:hover {
  cursor: pointer;
  fill: #333; /* Change fill color on hover */
}

.svg-icon:active {
  fill: #555; /* Change fill color on click */
}
</style>
