import Vue from 'vue';
import Router from 'vue-router';
import HomePage from '@/components/HomePage.vue';
import Login from '@/components/auth/Login.vue';
import Register from '@/components/user/Register.vue';
import ScheduledTaskIndex from '@/components/scheduledTasks/ScheduledTaskIndex.vue';
import ScheduledTaskEdit from '@/components/scheduledTasks/ScheduledTaskEdit.vue';
import TableMetadataIndex from '@/components/tablemetadata/TableMetadataIndex.vue';
import TableMetadataEdit from '@/components/tablemetadata/TableMetadataEdit.vue';
import NotificationRecipientIndex from '@/components/recipients/NotificationRecipientIndex.vue';
import NotificationLogIndex from '@/components/notifications/NotificationLogIndex.vue';
import Profile from '@/components/user/Profile.vue';

Vue.use(Router);

const routes = [
  {
    path: '/',
    name: 'HomePage',
    component: HomePage,
    meta: { requiresAuth: true }
  },
  {
    path: '/login',
    name: 'LoginPage',
    component: Login,
    meta: { requiresAuth: false, hideNavbar: true }
  },
  {
    path: '/register',
    name: 'RegisterPage',
    component: Register,
    meta: { requiresAuth: false, hideNavbar: true }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: Profile,
    meta: { requiresAuth: true }
  },
  {
    path: '/services',
    name: 'ScheduledTaskIndex',
    component: ScheduledTaskIndex,
    meta: { requiresAuth: true }
  },
  {
    path: '/services/:serviceName',
    name: 'ScheduledTaskEdit',
    component: ScheduledTaskEdit,
    props: true,
    meta: { requiresAuth: true }
  },
  {
    path: '/tables',
    name: 'TableMetadataIndex',
    component: TableMetadataIndex,
    meta: { requiresAuth: true }
  },
  {
    path: '/tables/:id',
    name: 'TableMetadataEdit',
    component: TableMetadataEdit,
    props: true,
    meta: { requiresAuth: true }
  },
  {
    path: '/recipients',
    name: 'NotificationRecipientIndex',
    component: NotificationRecipientIndex,
    meta: { requiresAuth: true }
  },
  {
    path: '/notificationlogs',
    name: 'NotificationLogIndex',
    component: NotificationLogIndex,
    meta: { requiresAuth: true }
  }
  
];

const router = new Router({
  mode: 'history',
  routes
});


router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  if (to.matched.some(record => record.meta.requiresAuth) && !token) {
    next('/login');
  } else {
    next();
  }
});



export default router;
