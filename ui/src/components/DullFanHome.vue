<template>
  <el-container class="layout-container-demo" style="height: 100vh">

    <el-container>
      <el-header class="text-left el-header-custom">
        <div class="toolbar">
          <el-text>SpringBoot-Generate</el-text>
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
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="端口" prop="ipPort">
        <el-input
            v-model="formLabelAlign.ipPort"
            placeholder="例如: 3306"
            clearable
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="用户名" prop="sqlUsername">
        <el-input
            v-model="formLabelAlign.sqlUsername"
            placeholder="例如: DullFan"
            clearable
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="密码" prop="sqlPassword">
        <el-input
            v-model="formLabelAlign.sqlPassword"
            placeholder="例如: 123456"
            clearable
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="数据库" prop="sqlPassword">
        <el-input
            v-model="formLabelAlign.sqlName"
            placeholder="例如: code_test"
            clearable
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
</style>
