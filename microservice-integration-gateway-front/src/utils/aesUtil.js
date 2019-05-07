const crypto = require('crypto')

const aesUtil = module.exports = {}

const key = '23be2b6201e41f10'

/**
 * aes加密
 * @param data 待加密内容
 * @param key 必须为32位私钥
 * @returns {string}
 */
aesUtil.encryption = function(data) {
  const clearEncoding = 'utf8'
  const cipherEncoding = 'base64'
  const cipherChunks = []
  const cipher = crypto.createCipheriv('aes-128-cbc', key, key)
  cipher.setAutoPadding(true)
  cipherChunks.push(cipher.update(data, clearEncoding, cipherEncoding))
  cipherChunks.push(cipher.final(cipherEncoding))
  return cipherChunks.join('')
}
