import { defineConfig } from 'cypress';

export default defineConfig({
  e2e: {},
  env: {
    'cypress-react-selector': {
      root: '#root'
    }
  }
});
