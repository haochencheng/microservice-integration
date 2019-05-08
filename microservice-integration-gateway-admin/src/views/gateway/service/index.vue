<template>
  <div class="app-container">

    <div class="filter-container">
      <el-input v-model="listQuery.serviceName" placeholder="服务名称" clearable style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-input v-model="listQuery.servicePath" placeholder="服务PATH" clearable style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter"/>
      <el-select v-model="listQuery.applicationDefinitionId" class="filter-item" clearable placeholder="所属应用">
        <el-option v-for="item in applicationDefinitionIdList" :key="item.id" :label="item.applicationName" :value="item.id"/>
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">搜索</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
    </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;">
      <el-table-column label="id" prop="id" sortable="custom" align="center" width="65">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="服务名称" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.serviceName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="服务path" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.servicePath }}</span>
        </template>
      </el-table-column>
      <!--<el-table-column label="方法类型" align="center">-->
      <!--<template slot-scope="scope">-->
      <!--<span>{{ scope.row.serviceMethod }}</span>-->
      <!--</template>-->
      <!--</el-table-column>-->
      <el-table-column v-if="false" label="所属应用Id" align="center">
        <template slot-scope="scope" hidden>
          <span>{{ scope.row.applicationDefinitionId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="所属应用" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.applicationName }}</span>
        </template>
      </el-table-column>
      <el-table-column :formatter="formatAuthorization" :prop="needAuthorization" label="授权类型" align="center"/>
      <el-table-column label="是否启用" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.enabled | statusFilter">{{ scope.row.enabled == true?'启用':'禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="描述" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.desc }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="150px" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleCreate()">新增</el-button>
          <el-button size="mini" type="success" @click="handleUpdate(scope.row)">修改
          </el-button>
          <el-button v-if="scope.row.enabled==true" size="mini" type="danger" @click="handleUpdateServiceEnabled(scope.row.id,'false')">禁用
          </el-button>
          <el-button v-if="scope.row.enabled!=true" size="mini" type="success" @click="handleUpdateServiceEnabled(scope.row.id,'true')">启用
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNo" :limit.sync="listQuery.pageSize" @pagination="getList" />
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <span :model="temp.id" hidden/>
        <el-form-item label="服务名称" prop="serviceName">
          <el-input v-model="temp.serviceName"/>
        </el-form-item>
        <el-form-item label="服务path" prop="servicePath">
          <el-input v-model="temp.servicePath"/>
        </el-form-item>
        <!--<el-form-item label="方法类型">-->
        <!--<el-select v-model="temp.serviceMethod" class="filter-item" placeholder="请选择方法类型">-->
        <!--<el-option v-for="item in serviceMethodOptions" :key="item.value" :label="item.label" :value="item.value"/>-->
        <!--</el-select>-->
        <!--</el-form-item>-->
        <el-form-item label="请选择所属应用">
          <el-select v-model="temp.applicationDefinitionId" class="filter-item" placeholder="请选择所属应用">
            <el-option v-for="item in applicationDefinitionIdList" :key="item.id" :label="item.applicationName" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="授权类型">
          <el-select v-model="temp.needAuthorization" class="filter-item" placeholder="请选择授权类型">
            <el-option v-for="item in needOptions" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="是否启用">
          <el-select v-model="temp.enabled" class="filter-item" placeholder="请选择是否启用">
            <el-option v-for="item in enabledOptions" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input :autosize="{ minRows: 2, maxRows: 4}" v-model="temp.desc" type="textarea" placeholder="请输入"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchList, enabledById, createService, updateService } from '@/api/serviceDefinition'
import waves from '@/directive/waves' // Waves directive
import Pagination from '@/components/Pagination'
import { fetchAvailableAppList } from '@/api/applicationDefinition'
const needAuthorizationENum = {
  '0': '不需要授权',
  1: '用户登录',
  2: '服务端通信',
  3: '授权访客模式'
}

export default {
  name: 'GateTable',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        true: 'success',
        false: 'danger'
      }
      return statusMap[status]
    }
  },
  data: function() {
    return {
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        pageNo: 1,
        pageSize: 10,
        serviceName: undefined,
        servicePath: undefined,
        applicationDefinitionId: undefined
      },
      appList: [],
      needAuthorization: '',
      dialogFormVisible: false,
      dialogVisible: false,
      statusOptions: ['create', 'update', 'deleted'],
      serviceMethodOptions: [{
        value: 'Get',
        label: 'Get'
      }, {
        value: 'Post',
        label: 'Post'
      }],
      needOptions: [{
        value: 0,
        label: '不需要授权'
      }, {
        value: 1,
        label: '用户登录'
      }, {
        value: 2,
        label: '服务端通信'
      }, {
        value: 3,
        label: '授权访客模式'
      }],
      enabledOptions: [{
        value: true,
        label: '启用'
      }, {
        value: false,
        label: '禁用'
      }],
      dialogStatus: null,
      textMap: {
        modify: '修改',
        create: '新增'
      },
      applicationDefinitionIdList: [{ '': '' }],
      temp: {
        id: undefined,
        serviceName: '',
        servicePath: '',
        serviceMethod: 'Get',
        applicationDefinitionId: '',
        applicationName: '',
        needAuthorization: 0,
        enabled: true,
        desc: ''
      },
      rules: {
        serviceName: [{ required: true, message: '请填写服务名称', trigger: 'change' }],
        servicePath: [{ required: true, message: '请填写服务路径', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    fetchAvailableAppList().then(response => {
      this.applicationDefinitionIdList = response
    })
  },
  methods: {
    getList() {
      this.listLoading = true
      const query = this.listQuery
      fetchList(query).then(response => {
        this.list = response.list
        this.total = response.total
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 0.5 * 1000)
      })
    },
    updateServiceEnabled: function(id, status) {
      const data = { id: id, enabled: status }
      let msg = ''
      if (status) {
        msg = '启用成功'
      } else {
        msg = '禁用成功'
      }
      enabledById(data).then(() => {
        this.total = 0
        this.list = null
        this.$message.success(msg)
        this.getList()
      })
    },
    handleUpdateServiceEnabled(id, status) {
      const msgMap = {
        true: '启用',
        false: '禁用'
      }
      this.$confirm(`确认${msgMap[status]}该服务吗？`)
        .then(() => {
          this.updateServiceEnabled(id, status)
        })
        .catch(_ => {})
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        serviceName: '',
        servicePath: '',
        needAuthorization: 0,
        serviceMethod: 'Get',
        applicationDefinitionId: '',
        applicationName: '',
        enabled: true,
        desc: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.dialogStatus = 'modify'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleFilter() {
      this.listQuery.pageNo = 1
      this.getList()
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createService(this.temp).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateService(tempData).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          })
        }
      })
    },
    formatAuthorization: function(row) {
      return needAuthorizationENum[row.needAuthorization]
    }
  }
}
</script>
