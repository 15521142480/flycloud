import type { BaseEntity } from '@/entity/base/base-entity'

export interface SysDept extends BaseEntity {
  code?: string
  name: string
  parentId: string
  sort: number
  leaderUserId?: string
  phone?: string
  email?: string
}

export interface SysPost extends BaseEntity {
  code: string
  name: string
  sort: number
}

export interface SysRole extends BaseEntity {
  type?: number
  name: string
  code: string
  sort: number
  roleMenuPermissionJson?: string
}

export interface SysMenu extends BaseEntity {
  type: number
  name: string
  parentId: string
  permission?: string
  buttonPermission?: string
  buttonPermissions?: SysMenuButtonPermission[]
  sort: number
  level?: number
  path: string
  icon?: string
  component?: string
  componentName?: string
  visible?: boolean
  keepAlive?: boolean
  alwaysShow?: boolean
  target?: boolean
  checked?: boolean
  children?: SysMenu[]
}

export interface SysMenuButtonPermission {
  btnName: string
  btnPermission: string
  checked?: boolean
}

export interface SysUser extends BaseEntity {
  account: string
  password?: string
  userType?: number
  loginType?: number
  name: string
  realName?: string
  avatar?: string
  email?: string
  telephone?: string
  birthday?: string
  sex?: number
  deptId?: string
  deptName?: string
  roleIds?: string
  roleNames?: string
}

export interface UserDetailInfo {
  user: SysUser
  permissionList: string[]
  menuTreeList: SysMenu[]
}

export interface SysUserRoleUpdate {
  userId: string
  roleIds: string[]
}

export interface SysDictType extends BaseEntity {
  name: string
  type: string
}

export interface SysDictData extends BaseEntity {
  sort: number
  label: string
  value: string
  dictType: string
  colorType?: string
  cssClass?: string
}

export interface RoleMenuPermission {
  menuId: string
  permission: string
}
