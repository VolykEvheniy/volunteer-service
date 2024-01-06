<template>
  <div class="adminPanel container p-5">
    <p class="text-center main-title">Керування сервісом</p>
    <div class="row">
      <div class="col-md-4">
        <div class="card point-card text-center text-dark">
          <div class="card-body">
            <h5 class="card-title p-4">Генерація токену</h5>
            <router-link :to="{name : 'Military'}">
              <button type="button" class="btn btn-primary btn-lg">Перейти</button>
            </router-link>
          </div>
        </div>
      </div>
      <div class="col-md-4">
        <div class="card point-card text-center text-dark">
          <div class="card-body">
            <h5 class="card-title p-4">Додання категорій</h5>
            <router-link :to="{name : 'AdminCategory'}">
              <button type="button" class="btn btn-primary btn-lg">Перейти</button>
            </router-link>
          </div>
        </div>
      </div>
      <div class="col-md-4">
        <div class="card point-card text-center text-dark">
          <div class="card-body">
            <h5 class="card-title p-4">Додання товарів</h5>
            <router-link :to="{name : 'AdminProduct'}">
              <button type="button" class="btn btn-primary btn-lg">Перейти</button>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  mounted() {
    if (!localStorage.getItem('token')) {
      this.$router.push({name : 'Signin'});
    }
  }
}
</script>

<style scoped>
.adminPanel{
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding-top: 50px;
}
.main-title {
  font-size: 2em;
  font-weight: 500;
}
.point-card {
  margin: 15px;
  box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);
  transition: 0.3s;
  border-radius: 5px;
  cursor: pointer;
}
.point-card:hover {
  box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2);
}
.card-body {
  padding: 20px;
}
.btn-primary {
  background-color: #007bff;
  color: white;
  border-color: transparent;
  border-radius: 0.25rem;
}
</style>