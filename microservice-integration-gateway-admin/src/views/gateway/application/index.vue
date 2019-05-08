<template>
  <div class="app-container">
    <div class="filter-container">
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
      <el-table-column label="应用名称" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.applicationName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="应用path" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.applicationPath }}</span>
        </template>
      </el-table-column>
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
          <el-button v-if="scope.row.enabled==true" size="mini" type="danger" @click="handleUpdateAppEnabled(scope.row.id,'false')">禁用
          </el-button>
          <el-button v-if="scope.row.enabled!=true" size="mini" type="success" @click="handleUpdateAppEnabled(scope.row.id,'true')">启用
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNo" :limit.sync="listQuery.pageSize" @pagination="getList" />
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <span :model="temp.id" hidden/>
        <el-form-item label="应用名称" prop="applicationName">
          <el-input v-model="temp.applicationName"/>
        </el-form-item>
        <el-form-item label="应用path" prop="applicationPath">
          <el-input v-model="temp.applicationPath"/>
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
import { fetchList, enabledById, createApp, updateApp } from '@/api/applicationDefinition'
import waves from '@/directive/waves' // Waves directive
import Pagination from '@/components/Pagination'
const msgMap = {
  true: '启用',
  false: '禁用'
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
        pageSize: 10
      },
      dialogFormVisible: false,
      dialogVisible: false,
      statusOptions: ['create', 'update', 'deleted'],
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
      temp: {
        id: undefined,
        applicationName: '',
        applicationPath: '',
        enabled: true,
        desc: ''
      },
      rules: {
        applicationName: [{ required: true, message: '请填写应用名称', trigger: 'change' }],
        applicationPath: [{ required: true, message: '请填写应用路径', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
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
    updateAppEnabled: function(id, status) {
      const data = { id: id, enabled: status }
      enabledById(data).then(() => {
        this.total = 0
        this.list = null
        this.$message.success(`${msgMap[status]}成功`)
        this.getList()
      })
    },
    handleUpdateAppEnabled(id, status) {
      this.$confirm(`确认${msgMap[status]}该应用吗？`)
        .then(() => {
          this.updateAppEnabled(id, status)
        })
        .catch(_ => {})
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        applicationName: '',
        applicationPath: '',
        enabled: false,
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
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createApp(this.temp).then(() => {
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
          updateApp(tempData).then(() => {
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
    }
  }
}
</script>
