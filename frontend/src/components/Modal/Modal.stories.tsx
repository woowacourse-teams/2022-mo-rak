import { Meta, Story } from '@storybook/react';
import Modal from './Modal';

export default {
  title: 'Reusable Components/Modal',
  component: Modal
} as Meta;

const DefaultTemplate: Story = (args) => (
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  <Modal isVisible={true} close={() => {}} {...args}>
    이것은 모달 공용 컴포넌트입니다
  </Modal>
);

const Default = DefaultTemplate.bind({});

export { Default };
