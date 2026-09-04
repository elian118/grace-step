import React from 'react';
import { Container } from '@/components/layout/views/Container.tsx';
import Header from '@/components/layout/views/Header.tsx';
import MainContent from '@/components/layout/views/MainContent.tsx';
import Footer from '@/components/layout/views/Footer.tsx';

const Layout = () => {
  return (
    <Container>
      <Header />
      <MainContent />
      <Footer />
    </Container>
  );
};

export default Layout;
