<template>
  <div class="container">
    <div class="row">
      <div class="col-12 justify-content-center d-flex flex-row pt-5">
        <div id="signin-div" class="flex-item border">
          <h2 class="pt-4 pl-4">Увійти</h2>
          <form @submit="signin" class="pt-4 pl-4 pr-4">
            <div class="form-group">
              <label class="token-label">Електронна пошта</label>
              <input
                  type="email"
                  class="form-control full-width-input"
                  v-model="email"
                  required
              />
            </div>
            <div class="form-group">
              <label class="token-label">Пароль</label>
              <input
                  type="password"
                  class="form-control full-width-input"
                  v-model="password"
                  required
              />
            </div>
            <button type="submit" class="btn btn-primary  btn-lg mt-2 py-0">
              Увійти
              <div
                  v-if="loading"
                  class="spinner-border spinner-border-sm"
                  role="status"
              >
                <span class="sr-only">Завантаження...</span>
              </div>
            </button>
          </form>
          <hr />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Signin",
  props: ["baseURL"],
  data() {
    return {
      email: null,
      password: null,
      loading: null,
    };
  },
  methods: {
    async signin(e) {
      e.preventDefault();
      this.loading = true;

      const user = {
        email: this.email,
        password: this.password,
      };

      await axios
        .post(`${this.baseURL}user/signIn`, user)
        .then((res) => {
          localStorage.setItem("token", res.data.token);
          this.$emit("fetchData");
          this.$router.push({ name: "Admin" });
        })
        .catch((err) => {
          swal({
            text: "Не вдалося увійти в систему!",
            icon: "error",
            closeOnClickOutside: false,
          });
          console.log(err);
        })
        .finally(() => {
          this.loading = false;
        });
    },
  },
  mounted() {
    this.loading = false;
  },
};
</script>

<style scoped>
.btn-primary {
  /*background-color: #f0c14b;*/
  /*color: black;*/
  /*border-color: #a88734 #9c7e31 #846a29;*/
  /*border-radius: 0;*/
  background-color: #007bff;
  color: white;
  border-color: transparent;
  border-radius: 0.25rem;
}

#signin-div {
  background-color: #f8f9fa;
  padding: 2rem;
  margin: auto;
  width: 60%;
  border-radius: 0.5rem;
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.1);
}

h2 {
  color: #6c757d;
  font-size: 2rem;
  font-weight: 400;
}

.full-width-input {
  width: 100%;
}

.token-label {
  font-size: 1.2em;
}

@media only screen and (min-width: 992px) {
  #signin-div {
    width: 40%;
  }
}
</style>
