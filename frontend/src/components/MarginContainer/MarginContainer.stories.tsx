import MarginContainer from '@/components/MarginContainer/MarginContainer';

import theme from '@/styles/theme';
import styled from '@emotion/styled';
import { Meta, Story } from '@storybook/react';

export default {
  title: 'Reusable Components/MarginContainer',
  component: MarginContainer
} as Meta;

const SampleBox = styled.div`
  width: 10rem;
  height: 10rem;
  border: 1px solid ${theme.colors.BLACK_100};
`;
const DefaultTemplate: Story = (args) => (
  <div>
    <SampleBox />
    <MarginContainer margin="4rem 0" {...args}>
      <SampleBox />
    </MarginContainer>
    <SampleBox />
  </div>
);

const Default = DefaultTemplate.bind({});

export { Default };
