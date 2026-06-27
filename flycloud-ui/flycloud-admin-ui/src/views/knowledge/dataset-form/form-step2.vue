<template>
  <el-row>
    <!-- Left Section -->
    <el-col :span="12">
      <el-card>
        <!-- 分段设置 -->
        <el-form>
          <el-form-item :label="t('auto.views.knowledge.dataset_form.form_step2.k14c6a5b1')">
            <el-radio-group v-model="segmentSetting">
              <el-radio :label="t('auto.views.knowledge.dataset_form.form_step2.k5b8959ed')">{{
                t('auto.views.knowledge.dataset_form.form_step2.k5b8959ed')
              }}</el-radio>
              <el-radio :label="t('auto.views.knowledge.dataset_form.form_step2.kc493338e')">{{
                t('auto.views.knowledge.dataset_form.form_step2.kc493338e')
              }}</el-radio>
            </el-radio-group>
          </el-form-item>

          <!-- 索引方式 -->
          <el-form-item :label="t('auto.views.knowledge.dataset_form.form_step2.kb6daa561')">
            <el-radio-group v-model="indexingMethod">
              <el-radio :label="t('auto.views.knowledge.dataset_form.form_step2.k491e64d8')">{{
                t('auto.views.knowledge.dataset_form.form_step2.k491e64d8')
              }}</el-radio>
              <el-radio :label="t('auto.views.knowledge.dataset_form.form_step2.kcd0bf688')">{{
                t('auto.views.knowledge.dataset_form.form_step2.kcd0bf688')
              }}</el-radio>
            </el-radio-group>
          </el-form-item>

          <!-- Embedding 模型 -->
          <el-form-item :label="t('auto.views.knowledge.dataset_form.form_step2.k5ac3380b')">
            <el-select v-model="embeddingModel" placeholder="Select Embedding Model">
              <el-option label="text-embedding-3-large" value="text-embedding-3-large" />
            </el-select>
          </el-form-item>

          <!-- 检索设置 -->
          <el-form-item :label="t('auto.views.knowledge.dataset_form.form_step2.k3ac7159d')">
            <el-card style="width: 400px">
              <div class="card-header">
                <span>{{ t('auto.views.knowledge.dataset_form.form_step2.k849c4e30') }}</span>
              </div>
              <el-slider v-model="topK" :min="1" :max="10" label="Top K" />
              <el-slider
                v-model="scoreThreshold"
                :min="0"
                :max="1"
                step="0.1"
                :label="t('auto.views.knowledge.dataset_form.form_step2.k3e31fb4b')"
              />
            </el-card>

            <el-card style="width: 400px">
              <div class="card-header">
                <span>{{ t('auto.views.knowledge.dataset_form.form_step2.k00df5eb2') }}</span>
              </div>
              <el-slider v-model="topK" :min="1" :max="10" label="Top K" />
              <el-slider
                v-model="scoreThreshold"
                :min="0"
                :max="1"
                step="0.1"
                :label="t('auto.views.knowledge.dataset_form.form_step2.k3e31fb4b')"
              />
            </el-card>

            <el-card style="width: 400px">
              <div class="card-header">
                <span>{{ t('auto.views.knowledge.dataset_form.form_step2.k50adb057') }}</span>
              </div>
              <el-slider v-model="topK" :min="1" :max="10" label="Top K" />
              <el-slider
                v-model="scoreThreshold"
                :min="0"
                :max="1"
                step="0.1"
                :label="t('auto.views.knowledge.dataset_form.form_step2.k3e31fb4b')"
              />
            </el-card>
          </el-form-item>
        </el-form>
      </el-card>
    </el-col>

    <!-- Right Section: 分段预览 -->
    <el-col :span="9">
      <el-card shadow="never">
        <div class="previews-title">{{
          t('auto.views.knowledge.dataset_form.form_step2.kd7673dfa')
        }}</div>
        <template v-for="(segment, index) in segmentPreviews" :key="index">
          <div class="segment-preview">
            <div class="title">
              <div class="left">
                <svg
                  width="12"
                  height="12"
                  viewBox="0 0 12 12"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    d="M4.74999 1.5L3.24999 10.5M8.74998 1.5L7.24998 10.5M10.25 4H1.75M9.75 8H1.25"
                    stroke="#98A2B3"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                </svg>
                <span class="id">{{ segment.number }}</span>
              </div>

              <div class="right">
                <svg
                  width="12"
                  height="12"
                  viewBox="0 0 12 12"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    d="M4 3.5H8M6 3.5V8.5M3.9 10.5H8.1C8.94008 10.5 9.36012 10.5 9.68099 10.3365C9.96323 10.1927 10.1927 9.96323 10.3365 9.68099C10.5 9.36012 10.5 8.94008 10.5 8.1V3.9C10.5 3.05992 10.5 2.63988 10.3365 2.31901C10.1927 2.03677 9.96323 1.8073 9.68099 1.66349C9.36012 1.5 8.94008 1.5 8.1 1.5H3.9C3.05992 1.5 2.63988 1.5 2.31901 1.66349C2.03677 1.8073 1.8073 2.03677 1.66349 2.31901C1.5 2.63988 1.5 3.05992 1.5 3.9V8.1C1.5 8.94008 1.5 9.36012 1.66349 9.68099C1.8073 9.96323 2.03677 10.1927 2.31901 10.3365C2.63988 10.5 3.05992 10.5 3.9 10.5Z"
                    stroke="#667085"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  />
                </svg>
                <span class="char-size">{{
                  t('auto.views.knowledge.dataset_form.form_step2.k332508f0')
                }}</span>
              </div>
            </div>

            <div class="content">{{ segment.text }}</div>
          </div>
        </template>
      </el-card>
    </el-col>
  </el-row>
</template>

<script setup>
import { ref } from 'vue'
const { t } = useI18n()
const segmentSetting = ref(t('auto.views.knowledge.dataset_form.form_step2.k5b8959ed'))
const indexingMethod = ref(t('auto.views.knowledge.dataset_form.form_step2.k491e64d8'))
const embeddingModel = ref('text-embedding-3-large')
const directionalSearch = ref(true)
const topK = ref(3)
const scoreThreshold = ref(0.5)

// Mock data for segment previews
const segmentPreviews = ref([
  { number: '001', text: t('auto.views.knowledge.dataset_form.form_step2.k5b276cd4') },
  { number: '002', text: t('auto.views.knowledge.dataset_form.form_step2.k5c7b55b2') },
  { number: '003', text: t('auto.views.knowledge.dataset_form.form_step2.kf69acb47') },
  { number: '004', text: t('auto.views.knowledge.dataset_form.form_step2.k47c879a3') },
  { number: '005', text: t('auto.views.knowledge.dataset_form.form_step2.kcb90f784') }
])
</script>

<style scoped lang="scss">
/* Add any custom styles here */

.previews-title {
  font-size: 18px;
  font-weight: 500;
}

.segment-preview {
  background-color: rgba(228, 228, 228, 0.38);
  border-radius: 10px;
  padding: 15px;
  margin-top: 15px;

  .title {
    display: flex;
    justify-content: space-between;

    .left {
      border-right: 5px;
      font-size: 13px;
      font-style: italic;
      font-weight: 500;
      color: #676767;
      box-sizing: border-box;
      align-items: center;

      .id {
        margin-left: 5px;
      }
    }

    .right {
      display: flex;
      flex-direction: row;
      align-items: center;

      .char-size {
        margin-left: 5px;
        font-size: 13px;
        color: rgba(57, 57, 57, 0.66);
      }
    }
  }

  .content {
    margin-top: 10px;
    font-size: 15px;
    font-weight: 500;
  }
}
</style>
