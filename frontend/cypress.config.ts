import { defineConfig } from 'cypress';

export default defineConfig({
  projectId: 'vs3n5w',
  e2e: {},

  env: {
    'cypress-react-selector': {
      root: '#root'
    }
  }
});
